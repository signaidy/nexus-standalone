-- mermaid.lua: renderiza bloques ```mermaid``` a PNG con mmdc y los inserta como imágenes

local counter = 0

local function is_windows()
  return package.config:sub(1,1) == '\\'
end

local function join(a, b)
  local sep = is_windows() and '\\' or '/'
  if a:sub(-1) == sep then return a .. b else return a .. sep .. b end
end

local function mkdir_p(path)
  if is_windows() then
    os.execute('if not exist "' .. path .. '" mkdir "' .. path .. '"')
  else
    os.execute('mkdir -p "' .. path .. '"')
  end
end

local outdir = ".mermaid-cache"
mkdir_p(outdir)

function CodeBlock(block)
  -- Detecta bloques de código con clase 'mermaid'
  local hasMermaid = false
  for _, cls in ipairs(block.classes) do
    if cls == "mermaid" then
      hasMermaid = true
      break
    end
  end
  if not hasMermaid then
    return nil
  end

  counter = counter + 1
  local base = string.format("mermaid-%03d", counter)
  local src  = join(outdir, base .. ".mmd")
  local png  = join(outdir, base .. ".png")

  -- Escribe el contenido del diagrama a archivo .mmd
  local f = assert(io.open(src, "w"))
  f:write(block.text)
  f:close()

  -- Ejecuta mmdc para generar PNG (compatible con tectonic/xelatex)
  local cmd = string.format('mmdc -i "%s" -o "%s"', src, png)
  local rc = os.execute(cmd)
  if rc ~= true and rc ~= 0 then
    io.stderr:write("mmdc falló con código " .. tostring(rc) .. " para " .. src .. "\n")
  end

  -- Inserta la imagen como bloque
  -- Puedes ajustar tamaño en LaTeX luego si quieres escalar.
  return pandoc.Para({ pandoc.Image({title = base}, png) })
end