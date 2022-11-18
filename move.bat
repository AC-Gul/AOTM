@echo off
title move

choice /N /C:wasdmgfp
IF ERRORLEVEL ==8 goto menu
IF ERRORLEVEL ==7 goto fire
IF ERRORLEVEL ==6 goto grab
IF ERRORLEVEL ==5 goto drop
IF ERRORLEVEL ==4 goto right
IF ERRORLEVEL ==3 goto down
IF ERRORLEVEL ==2 goto left
IF ERRORLEVEL ==1 goto up

:up
echo w>input.txt
goto end

:down
echo s>input.txt
goto end

:left
echo a>input.txt
goto end

:right
echo d>input.txt
goto end

:drop
echo m>input.txt
goto end

:grab
echo g>input.txt
goto end

:fire
echo f>input.txt
goto end

:menu
echo p>input.txt
goto end

:end
exit