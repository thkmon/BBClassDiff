# PNG 이미지의 흰색을 투명하게 변환하는 PowerShell 스크립트

Add-Type -AssemblyName System.Drawing

function Convert-WhiteToTransparent {
    param(
        [string]$ImagePath,
        [int]$Threshold = 250
    )
    
    try {
        # 이미지 로드
        $bitmap = [System.Drawing.Bitmap]::new($ImagePath)
        
        # 원본 백업
        $backupPath = $ImagePath + ".backup"
        if (-not (Test-Path $backupPath)) {
            Copy-Item $ImagePath $backupPath
        }
        
        # 각 픽셀 처리
        for ($y = 0; $y -lt $bitmap.Height; $y++) {
            for ($x = 0; $x -lt $bitmap.Width; $x++) {
                $pixel = $bitmap.GetPixel($x, $y)
                
                # 흰색에 가까운 색상을 투명하게 변환
                if ($pixel.R -ge $Threshold -and $pixel.G -ge $Threshold -and $pixel.B -ge $Threshold) {
                    $transparentColor = [System.Drawing.Color]::FromArgb(0, 255, 255, 255)
                    $bitmap.SetPixel($x, $y, $transparentColor)
                }
            }
        }
        
        # 저장
        $bitmap.Save($ImagePath, [System.Drawing.Imaging.ImageFormat]::Png)
        $bitmap.Dispose()
        
        Write-Host "처리 완료: $ImagePath" -ForegroundColor Green
        return $true
    }
    catch {
        Write-Host "처리 실패: $ImagePath - $_" -ForegroundColor Red
        return $false
    }
}

function Process-Directory {
    param(
        [string]$DirectoryPath,
        [int]$Threshold = 250
    )
    
    if (-not (Test-Path $DirectoryPath)) {
        Write-Host "디렉토리가 존재하지 않습니다: $DirectoryPath" -ForegroundColor Yellow
        return
    }
    
    $pngFiles = Get-ChildItem -Path $DirectoryPath -Filter "*.png" | Where-Object { $_.Extension -eq ".png" }
    
    if ($pngFiles.Count -eq 0) {
        Write-Host "PNG 파일이 없습니다: $DirectoryPath" -ForegroundColor Yellow
        return
    }
    
    Write-Host "`n=== PNG 파일 투명도 처리 시작 ===" -ForegroundColor Cyan
    Write-Host "디렉토리: $DirectoryPath"
    Write-Host "임계값: $Threshold"
    Write-Host "파일 수: $($pngFiles.Count)`n"
    
    $successCount = 0
    $failCount = 0
    
    foreach ($file in $pngFiles) {
        Write-Host "처리 중: $($file.Name) ... " -NoNewline
        if (Convert-WhiteToTransparent -ImagePath $file.FullName -Threshold $Threshold) {
            $successCount++
        } else {
            $failCount++
        }
    }
    
    Write-Host "`n=== 처리 완료 ===" -ForegroundColor Cyan
    Write-Host "성공: $successCount, 실패: $failCount"
    Write-Host "원본은 .backup 확장자로 저장되었습니다.`n"
}

# 메인 실행
Write-Host "PNG 이미지 투명도 변환 도구" -ForegroundColor Cyan
Write-Host "================================`n"

# icon 폴더 처리
Process-Directory -DirectoryPath "resources\icon" -Threshold 250

# button 폴더 처리
Process-Directory -DirectoryPath "resources\button" -Threshold 250

Write-Host "`n모든 작업이 완료되었습니다!" -ForegroundColor Green
Write-Host "문제가 있으면 .backup 파일로 복원할 수 있습니다."
