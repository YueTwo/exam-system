<#
clean-build.ps1
PowerShell script to remove common compiled/build/generated artifacts for this repo.
Usage:
  - Dry run (shows what would be removed):
      .\scripts\clean-build.ps1
  - Actually remove files:
      .\scripts\clean-build.ps1 -Remove
  - Force removal without confirmation:
      .\scripts\clean-build.ps1 -Remove -Force
#>

param(
    [switch]$Remove,
    [switch]$Force
)

$repoRoot = Split-Path -Parent $MyInvocation.MyCommand.Definition
$targets = @(
    "${repoRoot}\exam-api\target",
    "${repoRoot}\exam-api\target\**",
    "${repoRoot}\exam-api\target\*.jar",
    "${repoRoot}\exam-api\target\classes",

    "${repoRoot}\exam-vue\dist",
    "${repoRoot}\exam-vue\node_modules",

    "${repoRoot}\ignore\exam-api-target",
    "${repoRoot}\ignore\exam-vue-dist",

    "${repoRoot}\logs",
    "${repoRoot}\logs\**",

    "${repoRoot}\*.log",
    "${repoRoot}\**\*.log"
)

Write-Host "Repository root: $repoRoot" -ForegroundColor Cyan
Write-Host "The following paths are targeted for cleanup:" -ForegroundColor Cyan
$targets | ForEach-Object { Write-Host "  $_" }

if (-not $Remove) {
    Write-Host "\nDry run: no files will be deleted. Re-run with -Remove to actually delete." -ForegroundColor Yellow
    exit 0
}

foreach ($path in $targets) {
    # Use Get-Item/Get-ChildItem to expand wildcard patterns and check existence
    try {
        $items = Get-ChildItem -Path $path -Force -ErrorAction SilentlyContinue
    } catch {
        $items = $null
    }

    if ($items) {
        foreach ($it in $items) {
            if ($Force) {
                Remove-Item -LiteralPath $it.FullName -Recurse -Force -ErrorAction SilentlyContinue
                Write-Host "Removed: $($it.FullName)" -ForegroundColor Green
            } else {
                $confirm = Read-Host "Delete '$($it.FullName)'? (y/N)"
                if ($confirm -match '^[yY]') {
                    Remove-Item -LiteralPath $it.FullName -Recurse -Force -ErrorAction SilentlyContinue
                    Write-Host "Removed: $($it.FullName)" -ForegroundColor Green
                } else {
                    Write-Host "Skipped: $($it.FullName)" -ForegroundColor Yellow
                }
            }
        }
    } else {
        # Nothing matched this pattern
        # Uncomment next line to output non-existence lines if you want
        # Write-Host "No match for: $path" -ForegroundColor DarkGray
    }
}

Write-Host "Cleanup complete." -ForegroundColor Cyan
