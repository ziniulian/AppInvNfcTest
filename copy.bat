@echo off
xcopy L:\Doc\SVN\Work\TestNfc\trunk\src\TestNfc\app\src\main L:\Doc\Git\AppInvNfcTest\app\src\main\ /S
xcopy L:\Doc\SVN\Work\TestNfc\trunk\src\TestNfc\app\libs L:\Doc\Git\AppInvNfcTest\app\libs\ /S
copy L:\Doc\SVN\Work\TestNfc\trunk\src\TestNfc\app\build.gradle L:\Doc\Git\AppInvNfcTest\app
pause
