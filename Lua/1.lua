local function sort(a, b)
    if a.level > b.level then
        return true
    elseif a.level == b.level then
        if a.salary > b.salary then
            return true
        end
    end
    return false
end

local staff = {
    {id = 1, name = 'Kiiz', salary = 15000, level = 1},
    {id = 2, name = 'Oceuk', salary = 14000, level = 1},
    {id = 3, name = 'Henrin', salary = 23000, level = 2},
    {id = 4, name = 'Ocean', salary = 17000, level = 1},
}

table.sort(staff, sort)

for k, v in pairs(staff) do
    print('id=' .. v.id .. ' name=' .. v.name .. ' salary=' .. v.salary .. ' level=' .. v.level)
end