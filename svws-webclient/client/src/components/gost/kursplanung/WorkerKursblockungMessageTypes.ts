export type WorkerKursblockungMessageType = 'init' | 'next' | 'getErgebnisse';

export interface WorkerKursblockungMessage {
	cmd: WorkerKursblockungMessageType;
}

export interface WorkerKursblockungSendInit extends WorkerKursblockungMessage {
	cmd: 'init';
	faecher: string[];
	blockungsdaten: string;
}

export interface WorkerKursblockungReceiveInit extends WorkerKursblockungMessage {
	cmd: 'init';
}

export interface WorkerKursblockungSendNext extends WorkerKursblockungMessage {
	cmd: 'next';
	val?: number;
}

export interface WorkerKursblockungReceiveNext extends WorkerKursblockungMessage {
	cmd: 'next';
	result: boolean;
}

export interface WorkerKursblockungSendErgebnisse extends WorkerKursblockungMessage {
	cmd: 'getErgebnisse';
}

export interface WorkerKursblockungReceiveErgebnisse extends WorkerKursblockungMessage {
	cmd: 'getErgebnisse';
	result: string[];
}