local cities = {capital = 'Chengdu', 'Bazhong', 'Yaan', 'Panzhihua'}
local str1 = ''
local str2 = ''

for k, v in pairs(cities) do
    str1 = str1 .. v .. ' '
end

for k, v in ipairs(cities) do
    str2 = str2 .. v .. ' '
end

print(str1)
print(str2)