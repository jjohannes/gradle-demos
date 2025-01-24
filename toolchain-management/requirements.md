# Process for Downloading/Caching/Extracting Tools

## Status

- First GBE version with feature: `1.0.0`
    - First GBE version (Manifest download aspect): `3.0.0`
- Stable for download aspect
- Mostly stable for extraction aspect
    - legacy compatibility and performance issues, see: [Challenges of Extract Tool step](#challenges-of-extract-tool-step)

## Design

### User Viewpoint

**Project Responsible** identifies tool by ID and Version

```
tools {
  // tool(<NAME>, <VERSION>, <ALIAS-TO-REFER-TO-TOOL-IN-PROCESS>)
  tool("tresos", "29.1.0.1", "TRESOS")
}
```

**Project Responsible** uses tool in process

```
processes.process("tresos_gen"){
  tool("TRESOS") // declare that this process requires the tool
}
```

**Project Responsible** triggers a build that requires 'tresos_gen' - e.g.

```
./gradlew.bat :tresos_gen
```

### Viewpoint of GBE

1. **Identify required tools** Only when a process (Gradle tasks) is going to execute as part of a build, it is checked which tools are required.
   A tool is identified by `com.vitesco.tools:<NAME>:<VERSION>`.
   This happens before the process runs (Gradle configuration phase).
2. **Download/Cache Manifest** Here, the tool manifest is downloaded (and cached). It is a separate file (not in the tool Zip).
   The information from the manifest influences the configuration. For example,
   which Gradle tasks are needed for a process? Which other processes does the process depend on?
3. **Download/Cache Tool Zip** If the tool itself is not yet downloaded it will be downloaded and cached (as ZIP file) in Gradle's dependency cache.
   Since the tool can not change anymore without changing the version, Gradle will now always use the Zip from the cache (no repository connection required).
   This happens only right before the process starts (i.e. after predecessor processes ran).
4. **Extract Tool** Before the tool can be used, it needs to be extracted to a folder.
   This extraction, if not yet performed, needs to be done after the tool was downloaded and before it is used.

Steps 1, 2, 3 are implemented with Gradle core dependency management and Gradle standard approaches.
For step (4) we currently face a number of challenges.

### Challenges of 'Extract Tool' step

#### Requirements
- **R1** The extraction should only be done once
- **R2** The extraction should not be done again when the GBE version changes
- **R3** _[Legacy Tools Drive]_ Tools (Zips) not yet in artifactory should be consumable from the tools drive
- **(?) R4** _[Legacy Tool not Zipped]_
  It should be possible to skip the above steps 1-3 and instead of extracting the tool, copy it from the tools drive
- **R5** _[Scripts in projects relying on LegacyApps location]_
  The extraction should be done to 'C:/LegacyApps' (maybe only for certain tools)
- **(?) R6** _[Legacy Tools used and TD5 used]_
  If tools are copied to 'C:/LegacyApps' and TD5 is used on the same machine  it should not lead to errors (only due to R4 and R5)
- **R7** _[Some tools modify their own installation directory]_
  This is an issue with checking if an extracted tool is not broken: If the tool itself modifies its installation,
  a checksum check will fail (without specifying excluded)
- (added 06/01/25) **R8** If a tool is **not installed** and **not used** because tasks are taken FROM-CACHE it should not be downloaded/installed
- (added 20/01/25) **R9** Tools should be downloaded/extracted in parallel if tasks run in parallel

Added 2024-12-16
- **R8** Reuse a tool for different users on the same PC
- **R9** Provide a kind of tool clean up task (make it easy to delete all cached tools)

#### Discussion on requirements 2024-12-02

- See if we can drop **R4** and **R6** by rquiering all tools used in GBE
    1. Being zipped (on tools drive OR in artifactory) - solves R4
    2. Make sure that files are not write protected - solves R6

#### State of implementation

- R1 ✅
- R2 ❌
    - NEXT: explore solution(s) that do not use Gradle Artifact Transforms
- R3 ✅
- R4 ⚠️as Gradle's dependency management is not used, no caching; tools drive checked every time
    - NEXT: drop R4 (as discussed on 02-12-24)
- R5 ⚠️only for tools that are folders (R4)
    - NEXT: explore solution(s) - solving R2, R5, R7 together may make sense
- R6 ⚠️issues with files that are set to read-only
- R7 ❌
- R8 ❌

#### Missing solutions, interactions between solutions, open questions

- **R2**: Not working because we use Gradle's artifact transform. The extracted tool is put into Gradle's cache (location can not be configured).
  Tool is isolated there (which is good) but if GBE changes (new version) the cache is invalidated. All tools are extracted again.
  **Alternative (?)**: Implement the extraction ourselves (which could also help **R5**).
- **R4**: We should probably not invest more here, but require all tools to be zipped (?). If all tools are zipped, and the
  tools that are currently folders are released as Zip with a new version, we may not have bad interaction with TD5 anymore (R6).
- **R5**: If we support `LegacyApps` more (e.g. putting all tools there), issues R6 may get worse.
  What if a tool or user modifies the tool in `LegacyApps`?
- **R6**: Should be solved by improving on the other requirements or dropping some of them.
- **R7**: Need a information (in manifest?) about what to ignore in the installation (or not support this anymore?)
- **R8**: Cannot be solved if we want to use a Gradle "Work Unit" (Transform or Task) to do tool installation and caching

#### Implementation options if we want to support all requirements

- Our own Checksum Check in GBE to improve performance of R3 and possible provide an efficient solution for R5 (Sync from Gradle cache into C:/LegacyApps)
- Alternative: "Tool Sync" as a separate tool you need to run before GBE, which Syncs all tools. It can be slow, because users only run it manually when needed

## Build-Service based solution

Proof-of-concept: https://github.com/jjohannes/gradle-demos/tree/main/toolchain-management

- R1 ✅ - extraction only done if install folder was changed (using Gradle's FileSystemAccess service)
- R2 ✅ - same as R1
- R2 ✅ - same as R1
- R3 ✅ - still works by treating tools drive as repository with custom pattern
- R4 ✅ - Now supported directly in the installation service
- R5 ✅ - install location can be customized
- R6 ✅ - more control in the extraction process allows individual "special case" handling
- R7 ✅ - folder in installation can be excluded from check
- R8 ✅ - Installation service is used in parallel though Worker API

## Next Steps

- Integrate BuildService-base solution into TD5 (Basil, Jendrik)
- Test integrate solution into TD5 (Basil)
