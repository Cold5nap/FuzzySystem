export default class Type {
    constructor(name,terms) {
        this._name = name;
        this._terms = terms;
    }

    get name() {
        return this._name;
    }

    set name(value) {
        this._name = value;
    }

    get terms() {
        return this._terms;
    }

    set terms(value) {
        this._terms = value;
    }
}