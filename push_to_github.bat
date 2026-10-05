@echo off
chcp 65001 >nul
echo ========================================
echo   推送代码到 GitHub
echo ========================================
echo.

set /p REPO_URL="请输入你的GitHub仓库地址(例如: https://github.com/username/fore-study.git): "
echo.

if "%REPO_URL%"=="" (
    echo ❌ 仓库地址不能为空！
    pause
    exit /b 1
)

echo 正在添加远程仓库...
git remote add origin %REPO_URL%

echo.
echo 正在重命名分支为main...
git branch -M main

echo.
echo 正在推送到GitHub...
echo.
echo ⚠️ 如果提示输入用户名密码，请输入:
echo   - 用户名: 你的GitHub用户名
echo   - 密码: Personal Access Token (不是GitHub密码)
echo   获取Token: https://github.com/settings/tokens/new
echo.
echo 开始推送...
git push -u origin main

if %errorlevel% equ 0 (
    echo.
    echo ✅ 推送成功！
    echo.
    echo 你可以在这里查看你的代码:
    echo %REPO_URL%
) else (
    echo.
    echo ❌ 推送失败，请检查错误信息
)

echo.
pause