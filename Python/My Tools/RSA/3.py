# 定义输入和输出文件路径
input_file_path = 'yuan.txt'  # 输入文件（由上一个脚本生成）
output_file_path = 'filtered_output1.txt'

# 读取原始内容并过滤
with open(input_file_path, 'r') as f:
    lines = f.readlines()

# 过滤掉任意 RGB 值 >= 100 的行
filtered_lines = []
for line in lines:
    parts = line.strip().split(',')
    if len(parts) != 3:
        continue  # 如果格式不正确，跳过

    r, g, b = map(int, parts)

    # 判断是否任意一个值 >=100（只要有一个就删除）
    if r < 100 or g < 100 or b < 100:
        continue  # 删除该行

    filtered_lines.append(line)

# 写入过滤后的结果到新文件
with open(output_file_path, 'w') as f:
    f.writelines(filtered_lines)

print(f"已删除 RGB 值 ≥100 的行，结果保存至：{output_file_path}")
