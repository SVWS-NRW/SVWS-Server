process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';

const originalEmitWarning = process.emitWarning.bind(process);

process.emitWarning = (warning, ...args: any[]) => {
	if (typeof warning === 'string' && warning.includes('NODE_TLS_REJECT_UNAUTHORIZED')) {
		return;
	}
	return originalEmitWarning(warning, ...args);
};