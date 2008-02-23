@echo off
rem complete your IDEA Home folder here
set IDEA_HOME=C:\Program Files\JetBrains\IntelliJ IDEA 7.0.2
set IDEA_VERSION=7.0.2
echo.
echo Installing packages of IDEA version %IDEA_VERSION% from "%IDEA_HOME%" to the local Maven repository...
echo.
call mvn install:install-file -Dfile="%IDEA_HOME%\redist\annotations.jar"         -DgroupId=com.intellij -DartifactId=annotations -Dversion=%IDEA_VERSION% -Dpackaging=jar -DcreateChecksum=true -DgeneratePom=true
call mvn install:install-file -Dfile="%IDEA_HOME%\redist\forms_rt.jar"            -DgroupId=com.intellij -DartifactId=forms_rt    -Dversion=%IDEA_VERSION% -Dpackaging=jar -DcreateChecksum=true -DgeneratePom=true
call mvn install:install-file -Dfile="%IDEA_HOME%\redist\javac2.jar"              -DgroupId=com.intellij -DartifactId=javac2      -Dversion=%IDEA_VERSION% -Dpackaging=jar -DcreateChecksum=true -DgeneratePom=true
call mvn install:install-file -Dfile="%IDEA_HOME%\redist\src\src_annotations.zip" -DgroupId=com.intellij -DartifactId=annotations -Dversion=%IDEA_VERSION% -Dpackaging=jar -DcreateChecksum=true -Dclassifier=sources
call mvn install:install-file -Dfile="%IDEA_HOME%\redist\src\src_forms_rt.zip"    -DgroupId=com.intellij -DartifactId=forms_rt    -Dversion=%IDEA_VERSION% -Dpackaging=jar -DcreateChecksum=true -Dclassifier=sources
call mvn install:install-file -Dfile="%IDEA_HOME%\redist\src\src_javac2.zip"      -DgroupId=com.intellij -DartifactId=javac2      -Dversion=%IDEA_VERSION% -Dpackaging=jar -DcreateChecksum=true -Dclassifier=sources
echo.
pause
