package dev.zwazel.autobattler.classes.abstractClasses;

@FunctionalInterface
public interface ScaleAttributeWithLevel {
    int scale(int attribute, int level);
}
