@echo off
chcp 65001 >nul
echo ========================================
echo   ????10000???????...
echo ========================================
echo.
mysql -u root -p18170692219asd. fore_shop < "D:\software\fore-study\src\main\resources\sql\generate_products.sql"
if %errorlevel% equ 0 (
    echo.
    echo ? ???????
) else (
    echo.
    echo ? ????????????
)
pause
