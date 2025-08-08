# Đường dẫn đến repo
$repoPath = "D:\Xmax\Alarm_clock\Alarm_clock"
cd $repoPath

# Kiểm tra thay đổi
$changes = git status --porcelain

if ($changes) {
    git add .
    $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    git commit -m "Auto commit: $timestamp"
    git push origin main
    Write-Host "✅ Đã commit và push lúc $timestamp"
} else {
    Write-Host "⏸️ Không có thay đổi nào để commit."
}