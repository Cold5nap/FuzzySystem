import InputVariable from "@/services/InputVariable";

export default class OutputVariable extends InputVariable{
    constructor(name,type,def,method) {
        super(name,type);
        this._def = def;
        this._method = method;
    }

    get def() {
        return this._def;
    }

    set def(value) {
        this._def = value;
    }

    get method() {
        return this._method;
    }

    set method(value) {
        this._method = value;
    }
}