import argparse
from scanner import Default, Length, Cookie

def get_input() -> object:
    parser = argparse.ArgumentParser()
    parser.add_argument('-U', type=str, help='target URL', required=True)
    parser.add_argument('-D', type=str, default='default.txt', help='the dictionary to be used, default=default.txt')
    parser.add_argument('-M', type=str, default='Default' , choices=['Default', 'Length', 'Cookie'], help='''the mode of dirscan,
                        "Default" is to collect all the responses with http code 200,
                        "Length" is to collect all the responses with a length different from "index.php",
                        "Cookie" is to collect all the responses with a blank cookie''')
    parser.add_argument('-O', type=str, default='./dirscan_dict/output/', help='where the output file to save')
    try:
        args = parser.parse_args()
    except:
        exit()
    return args

def choices(args: object) -> None:
    mode = args.M
    if mode == 'Default':
        scanner = Default(args.U, args.D, args.O)
    elif mode == 'Length':
        scanner = Length(args.U, args.D, args.O)
    elif mode == 'Cookie':
        scanner = Cookie(args.U, args.D, args.O)
    scanner.scan()
    scanner.out()

if __name__ == '__main__':
    args = get_input()
    choices(args)