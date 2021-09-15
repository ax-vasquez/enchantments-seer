# Understanding `BiFunction`
`BiFunction` is a class used frequently by Mekanism. Understanding how it works, how to use it, and most-importantly 
**when** to use it will go a long way in understanding our mod.

### Basic information
`BiFunction<T, U, R>`
* **Package:** `java.util.function`
* **Type:** `interface`
* Resources
  * [Baeldung](https://www.baeldung.com/java-bifunction-interface)

### Notes
**Definition:** _A `BiFunction` is a class-representation of a binary function, which is a functional interface that
uses two parameters._

### "Live" examples
These are examples within the `enchantments-seer` codebase where we use the `BiFunction` class.

#### `CapabilityHandlerManager`
This class is a general capability handler class that we shamelessly-copied from Mekanism. Its purpose is to be a flexible
manager class for _any_ given capability, built-in, or custom. 

We only use this class for handling items in our mod, which will always have a corresponding "holder" class. As a result,
_in our usage of `CapabilityHandlerManager`, `canHandle` will always be `true`_. Because of this, when `getContainers` 
is called, we will always use `containerGetter` to get the list of `CONTAINER`s (which is a generic - for our mod, this 
will always be an inventory container of some kind).

**`containerGetter` is a `BiFunction`**. It's definition:
```java
private final BiFunction<HOLDER, Direction, List<CONTAINER>> containerGetter;
```
* This is the exact definition from Mekanism - **we don't need to use `Direction`**

