export interface SchuleDatenaustauschWenomProps {
    secretSet: () => boolean;
    setWenomCredentials: (url: string, token: string) => Promise<void>;
    wenomSynchronize: () => Promise<void>;
    wenomTruncate: () => Promise<void>;
    wenomRemoveCredentials: () => Promise<void>;
}

