local function sum(a, b)

    return a + b
end

local function input()

    local a = io.read()
    local b = io.read()

    return a, b
end

local a, b = input()
local code, reason = pcall(sum, a, b)
print(code, reason)