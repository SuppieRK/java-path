Filter Operators
-----------------

Filters are logical expressions used to filter arrays. A typical filter would be `[?(@.age > 18)]` where `@` represents the current item being processed. More complex filters can be created with logical operators `&&` and `||`. String literals must be enclosed by single or double quotes (`[?(@.color == 'blue')]` or `[?(@.color == "blue")]`).   

| Operator                   | Description                                                               |
| :------------------------- | :------------------------------------------------------------------------ |
| `==`                       | left is equal to right (note that `1` is not equal to `'1'`)              |
| `!=`                       | left is not equal to right                                                |
| `<`                        | left is less than right                                                   |
| `<=`                       | left is less or equal to right                                            |
| `>`                        | left is greater than right                                                |
| `>=`                       | left is greater than or equal to right                                    |
| `=~`                       | left matches regular expression `[?(@.name =~ /foo.*?/i)]`                |
| `in`                       | left exists in right `[?(@.size in ['S', 'M'])]`                          |
| `nin`                      | left does not exists in right                                             |
| `subsetof`                 | left is a subset of right `[?(@.sizes subsetof ['S', 'M', 'L'])]`         |
| `anyof`                    | left has an intersection with right `[?(@.sizes anyof ['M', 'L'])]`       |
| `noneof`                   | left has no intersection with right `[?(@.sizes noneof ['M', 'L'])]`      |
| `size`                     | size of left (array or string) should match right                         |
| `empty`                    | left (array or string) should be empty                                    |
