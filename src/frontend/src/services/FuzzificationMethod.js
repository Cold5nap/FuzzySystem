export default class FuzzificationMethod {
    constructor(name,points) {
        this._name = name;
        this._points = points;
    }

    get name() {
        return this._name;
    }

    set name(value) {
        this._name = value;
    }

    get points() {
        return this._points;
    }

    set points(value) {
        this._points = value;
    }
}