# Individual

The `Individual` class is used to represent a single person in a GEDCOM file. 

## Traversals

Instructions and code sinppets for moving between individuals.

### *Down*

Moving *down*, through the decendants of an individual can be done via the `getChildren()` method by repeatedly getting the children of subseuent generations. 

Ex: Getting grand-children
```java
List<Individual> grandChildren = new ArrayList<>();

for (Individual child : individual.getChildren()) {
    grandChildren.addAll(child.getChildren());
}
```

### *Up*

Moving *up*, through the ancestors of an individual can be done via the `getParents()` method by repeatedly getting the parents of previous generations.


Ex: Getting grand-parents
```java
List<Individual> grandParents = new ArrayList<>();

for (Individual parent : individual.getParents()) {
    grandParents.addAll(child.getParents());
}
```
