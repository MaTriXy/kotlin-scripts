# kotlin-scripts

## setup


```
brew install maven
```

Demos using kotlin as a scripting language

## Running this with native kotlin compiler

```
brew install kotlinc
kotlinc -script list_folders.kts ~
```

## Running this with kscript

```
// install sdkman (if you haven't already)
curl -s "https://get.sdkman.io" | bash  # install sdkman
sdk install maven
sdk install kotlin
sdk install kscript

// alternatively with homebrew
brew install maven
brew install holgerbrandl/tap/kscript

kscript -script list_folders.kts ~

# as it stands kscript does some caching which might make the program run faster
```


### Hackathon voting

```
  cd /directory/with/csv/file
  kscript /code/path/carrot-wars-tabulate.kts /carrot-wars-results.csv
```
