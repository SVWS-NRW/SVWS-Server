<template>
	<div class="page page-flex-row">
		<div class="min-w-fit flex flex-col gap-8">
			<div class="min-w-148 max-w-196">
				<div class="text-headline-md mb-4 flex flex-row items-center gap-2">
					<svws-ui-tooltip autosize>
						<span v-if="manager().auswahl().serverTLSCertIsTrusted" class="icon-lg i-ri-verified-badge-fill icon-ui-success" />
						<span v-else class="icon-lg i-ri-error-warning-fill icon-ui-danger" />
						<template #content v-if="manager().auswahl().serverTLSCertIsTrusted && (validCert !== null)">
							<div>Diese Verbindung ist sicher</div>
							<div>Das verwendete Zertifikat wurde ausgestellt von <span class="font-bold">{{ validCert.issuer }}</span></div>
							<div>Inhaberin dieses Zertifikats ist <span class="font-bold">{{ validCert.subject }}</span></div>
							<div>Es ist gütig von {{ validCert.validSince }} bis {{ validCert.validUntil }}</div>
						</template>
						<template #content v-else>
							<div>Diese Verbindung ist nicht sicher</div>
							Bitte prüfen Sie, ob diesem Zertifikat vertraut werden kann.
						</template>
					</svws-ui-tooltip>
					<span class="">{{ manager().auswahl().url }}</span>
					<div><a :href="manager().auswahl().url" target="_blank" rel="noopener noreferrer"><span class="icon i-ri-link cursor-pointer" /></a></div>
				</div>
				<svws-ui-input-wrapper v-if="!manager().auswahl().serverTLSCertIsTrusted">
					<div v-if="cert === null">Kein Zertifikat angegeben.</div>
					<div v-else class="flex flex-col gap-2 mb-4">
						<div>
							<div class="font-bold">Unbekanntes Zertifikat</div>
							<div><span class="font-bold">Inhaber:</span> {{ cert.subject }}</div>
							<div><span class="font-bold">Aussteller:</span> {{ cert.issuer }}</div>
							<div><span class="font-bold">Gültigkeit:</span> {{ }} bis {{ cert.validUntil !== null ? DateUtils.gibDatumGermanFormat(cert.validUntil) : 'Fehler' }}</div>
						</div>
						<div class="bg-ui-warning rounded-md p-2 text-ui-onwarning">
							Achtung, Sie können nur fortfahren mit der Einrichtung des WebNotenManagers, wenn Sie diesem Zertifikat vertrauen.
							Eine Bestätigung des Zertifikats schaltet die angegebene Verbindung für den aktiven Datenaustausch frei, bei dem auch personenbezogene Daten übermittelt werden.
							Bestätigen Sie diesen Schritt nur, wenn Sie sicher sind, dass diese Verbindung vertrauenswürdig ist.
							<br>Wenn Sie sich unsicher sind, fragen Sie bitte Ihren IT-Support.
						</div>
						<svws-ui-button @click="trustCertificate(true)"> Zertifikat vertrauen </svws-ui-button>
					</div>
				</svws-ui-input-wrapper>
				<svws-ui-input-wrapper v-if="manager().auswahl().serverTLSCertIsTrusted">
					<div v-if="manager().getAuswahlConnectionResponse()?.success === false" class="bg-ui-warning rounded-md p-2">
						Um Daten mit dem ENM-Server austauschen zu können, muss das auf dem Webspace abgelegte Secret ausgelesen und in das unten angegebene Feld eingefügt werden.
						<br>Standardmäßig befindet sich das Secret in der Datei <span class="font-mono">db/client.sec</span> ihres Webspace.
					</div>
					<svws-ui-text-input v-if="manager().getAuswahlSetupResponse() !== null" :model-value="manager().auswahl().clientSecret" type="password" placeholder="Secret" @change="clientSecret => (clientSecret !== null) && updateServerConnection({ clientSecret })" required />
					<svws-ui-text-input :model-value="manager().auswahl().bezeichnung" type="text" placeholder="Bezeichnung" @change="bezeichnung => updateServerConnection({ bezeichnung: bezeichnung ?? null })" />
					<svws-ui-button type="primary" @click="connect(manager().auswahl().id)">
						Verbindungsdaten prüfen
					</svws-ui-button>
				</svws-ui-input-wrapper>
			</div>
		</div>

		<!-- Die Ausgabe des Logs -->
		<div v-if="status !== null && manager().auswahl().id > 0" class="min-w-fit grow h-full overflow-hidden flex flex-col gap-4">
			<log-box :logs="status.log" :status="status.success" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import type { NotenmodulVerbindungProps } from './NotenmodulVerbindungProps';
	import type { TLSCertificate } from '@core/core/data/TLSCertificate';
	import { DateUtils } from '@core/core/utils/DateUtils';

	const props = defineProps<NotenmodulVerbindungProps>();


	const cert = computed<TLSCertificate | null>(() => {
		const connInfo = props.manager().auswahl();
		if ((connInfo.serverTLSCertChain.size() < 1)) {
			return null;
		}
		return connInfo.serverTLSCertChain.getFirst();
	});

	const status = computed(() => props.manager().getAuswahlConnectionResponse());

	const validCert = computed(() => {
		if (cert.value === null) {
			return null;
		}
		const issuerArr = cert.value.issuer?.split(',') ?? [];
		const subjectArr = cert.value.subject?.split(',') ?? [];
		const c = {
			issuer: issuerArr.find(i => i.startsWith('O='))?.slice(2) ?? 'Fehler',
			subject: subjectArr.find(i => i.startsWith('CN=') || i.startsWith('O='))?.slice(3) ?? 'Fehler',
			validSince: cert.value.validSince === null ? 'Fehler' : DateUtils.gibDatumGermanFormat(cert.value.validSince),
			validUntil: cert.value.validUntil === null ? 'Fehler' : DateUtils.gibDatumGermanFormat(cert.value.validUntil),
		};
		return c;
	});

</script>
