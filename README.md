Packs Entries Blocker is for avoiding identifier matches the regex from loading from specified pack.  

## Use Case
- Too many mods find their resources from packs will affect the launching time such as GeckoLib, Optifine-like. Profiling it with https://modrinth.com/mod/more-profiling
- Disable some entries needn't in packs like some CITs

## How to
1. Modify the config at `config/pack-entries-blocker.json`. Will reload before data/resource packs reloading.
2. Blocked entries will print to `debug.log` for debugging.
  - How to access `debug.log`?
    - https://wiki.vg/Debugging
    - https://modrinth.com/mod/better-log4j-config and edit it's `better_log4j_config.xml` then
