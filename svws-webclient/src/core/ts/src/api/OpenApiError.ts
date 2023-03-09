
export class OpenApiError extends Error {

    _status : number | null;
    _error : Error | null;

    public constructor(value : Response | Error, message? : string) {
        super(message);
        this._status = (value instanceof Error) ? null : value.status;
        this._error = (value instanceof Error) ? value : null;
    }

    public get status() : number | null {
        return this._status;
    }

    public get error() : Error | null {
        return this._error;
    }

}
