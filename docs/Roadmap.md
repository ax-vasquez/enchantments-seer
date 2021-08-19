# Roadmap

## Project philosophies
1. **Never make the code proprietary**
    1. *MIT license*
    2. *Not only allow changes, but actively encourage and teach others how to make changes/implement their own version*
2. **Write code as though someone else will pick up from where you left off**
    1. *Ample documentation*
    2. *Code samples*
3. **Light-touch**
    1. *Re-use as many assets as possible*
    2. *Don't add new enchantments*

## To do
1. [ ] Seer's Enchantment table
   1. [ ] GUI
      1. [ ] Book reagent slot
         1. [ ] 
      2. [ ] Item reagent slot
      3. [ ] Item output slot
      4. [ ] Enchantments list
         1. [ ] With sliders
   2. [ ] Model (based off the regular enchanting table)
      1. [ ] Without any book stored in the book reagent slot
      2. [ ] With a regular enchantment book stored in the book reagent slot
      3. [ ] With a Seer's Manuscript stored in the book reagent slot
   3. [ ] Logic
      1. [ ] Mirror logic of vanilla enchantment table
      2. [ ] Book reagent slot is a persistent storage container (so players can place books for others to use later)
      3. [ ] With Seer's Manuscript as book reagent
         1. [ ] Display **all** enchantments
         2. [ ] Only one enchantment can be added at a time
            1. [ ] Expose configuration to disable this functionality (so that any enchantments can be added using a single Seer's Manuscript)
            2. [ ] Expose configuration to set recharging enchantment cost to either linear or static (maybe scaling as well)
      4. [ ] With arbitrary enchanted book as book reagent
         1. [ ] Only expose the enchantment(s) that the book has and allow the user to enchant up to max value without spending experience/levels
   4. [ ] Recipe
2. [ ] Seer's Stone
   1. [ ] Model (based off the vanilla Diamond item model)
   2. [ ] Recipe
3. [ ] Seer's Manuscript
   1. [ ] Model (based off the vanilla book item model)
   2. [ ] Recipe