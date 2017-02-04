[![Build Status](https://travis-ci.org/palmfjord/twig4j-core.svg?branch=master)](https://travis-ci.org/palmfjord/twig4j-core)

# Twig for Java

This is a Java implementation of the [Twig templating engine](http://twig.sensiolabs.org).

## About the project
The project is still under heavy development. To see what currently works my please look at
 [the functional tests (src/tests/java/org/twig/functional)](https://github.com/palmfjord/twigjava/tree/master/src/test/java/org/twigjava/functional)
 as that's where all functionality (such as render variables, maths, method calls, etc.) is tested.

Since Java and PHP are relatively different (especially when it comes to typing) some things available in the original
 Twig library probably won't work in a Java port, but the intention is to make a library where you can just take the
 Twig templates written for the PHP implementation of Twig and start using them with the Java implementation.
 Unfortunately we haven't managed to solve how to compare different types (i.e. 1 == "1"), so *this implementation of twig
 will throw an exception when comparing different types* - the alternative would be to fail to compare silently. A solution
 to this is more than welcome!

If you want to contribute or have feedback of any kind feel free to drop an email to johan.palmfjord@gmail.com :)

### To do/upcoming
Not everything is listed here but this is what's prioritized currently:
 - User-defined functions (that aren't "hard-coded" in the parser)
 - Gloabal variables
 - Use tags/traitable blocks: `{% use 'foo.twig' %}`
 - Macros
