Option Explicit    
    dim shell, shortcutFileName, shortcutLink, targetPath, currDir    
    Set shell    = WScript.CreateObject("WScript.Shell")    
    shortcutFileName = "C:\downloads\a.lnk"   
    Set shortcutLink = shell.CreateShortcut(shortcutFileName)    
    targetPath = shortcutLink.TargetPath    
    currDir = shell.CurrentDirectory    
    If targetPath = "" then targetPath = "Nothing_Or_Error"   
    shell.run("java.exe -classpath """&currDir&""" GetShortcutPath " & """&targetPath&""")   
    Set shortcutLink = Nothing    
    Set shell    = Nothing  