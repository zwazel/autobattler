class Unit {
    constructor(type, side, id, name, level, position, priority, image) {
        this._type = type;
        this._side = side;
        this._id = id;
        this._name = name;
        this._level = level;
        this._position = position;
        this._priority = priority;
        this._image = image;
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
}