function scaleAttribute(level, attribute, scaleFunction) {
    return parseInt(scaleFunction(level, attribute));
}

function scaleAttributeDefault(level, attribute) {
    return parseInt(attribute + ((level - 1) * 10));
}

class Unit {
    constructor(type, side, id, name, level, health, position, priority, image) {
        this._type = type;
        this._side = side;
        this._id = id;
        this._name = name;
        this._level = level;
        this._position = position;
        this._priority = priority;
        this._image = image;
        this._health = health;
    }

    get type() {
        return this._type;
    }

    set type(value) {
        this._type = value;
    }

    get side() {
        return this._side;
    }

    set side(value) {
        this._side = value;
    }

    get id() {
        return this._id;
    }

    set id(value) {
        this._id = value;
    }

    get name() {
        return this._name;
    }

    set name(value) {
        this._name = value;
    }

    get level() {
        return this._level;
    }

    set level(value) {
        this._level = value;
    }

    get position() {
        return this._position;
    }

    set position(value) {
        this._position = value;
    }

    get priority() {
        return this._priority;
    }

    set priority(value) {
        this._priority = value;
    }

    get image() {
        return this._image;
    }

    set image(value) {
        this._image = value;
    }

    get health() {
        return this._health;
    }

    set health(value) {
        this._health = value;
    }
}