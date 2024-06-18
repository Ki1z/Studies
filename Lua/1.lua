local array = {'Jack', 'Tom', 'Ming'}

local function Iterator(array)
    local index = 0
    local length = #array

    return function ()
        index = index + 1
        if index <= length then
            return array[index]
        end
    end
end

for i in Iterator(array) do
    print(i)
end