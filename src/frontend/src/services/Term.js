export default class Term {
    constructor(name,termFunction) {
        this._name = name;
        this._termFunction = termFunction;
    }

    get name() {
        return this._name;
    }

    set name(value) {
        this._name = value;
    }

    get termFunction() {
        return this._termFunction;
    }

    set termFunction(value) {
        this._termFunction = value;
    }
}