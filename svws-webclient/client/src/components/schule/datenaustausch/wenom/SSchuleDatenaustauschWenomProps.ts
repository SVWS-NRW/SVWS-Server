export interface SchuleDatenaustauschWenomProps {
    setWenomCredentials: (url: string, token: string) => Promise<void>;
    wenomSynchronize: () => Promise<void>;
    wenomTruncate: () => Promise<void>;
}

