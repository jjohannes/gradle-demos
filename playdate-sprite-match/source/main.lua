import "CoreLibs/sprites"

local entries = 20
local border = 24
local size = 20
local scale = 8

local table = playdate.graphics.imagetable.new("images/pictures")

local delta = border
local onScreen = 1
local score = 0

local shuffledIndices
local onScreenCounter
local toFind

local function contains(val)
    for i=1,#shuffledIndices do
        if shuffledIndices[i] == val then
            return true
        end
    end
    return false
end

local function restart()
    shuffledIndices = {}
    onScreenCounter = 0
    toFind = math.random(entries)
    while (toFind == onScreen) do
        toFind = math.random(entries)
    end
    for i=1,entries do
        local j = math.random(table:getLength())
        while contains(j) do
            j = math.random(table:getLength())
        end
        shuffledIndices[i] = j
    end
end

restart()
function playdate.update()
    playdate.graphics.clear()

    local c1, c2 = playdate.getCrankChange()
    delta = delta + c1
    if delta > border then
        delta = border
    end

    local lowerBound = -1 * (#shuffledIndices) * size * scale + 240
    if delta < lowerBound then
        delta = lowerBound
    end

    local nextOnScreen = math.floor(1.85 + (-1 * delta / size / scale))
    if onScreen == nextOnScreen then
        onScreenCounter = onScreenCounter + 1
    else
        onScreenCounter = 0
        onScreen = nextOnScreen
    end

    for n=1, #shuffledIndices do
        table:getImage(shuffledIndices[n]):drawScaled(border, delta + size * scale * (n -1), scale)
    end
    table:getImage(shuffledIndices[toFind]):drawScaled(400 - border - size * scale, 60, scale)
    playdate.graphics.drawText("*"..score.."*", 360, border)

    if c1 > 0 or c1 < 0 then
        onScreenCounter = 0
    end
    if onScreenCounter > 30 and onScreen == toFind then
        score = score + 1
        restart()
    end
end
