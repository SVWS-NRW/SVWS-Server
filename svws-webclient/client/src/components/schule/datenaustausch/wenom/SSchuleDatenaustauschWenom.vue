<template>
	<div class="flex flex-col w-full h-full overflow-hidden">
		<svws-ui-header>
			<span class="inline-block mr-3">Datenaustausch mit dem Web-Notenmodul</span>
			<br>
			<span class="opacity-50 flex">
				<span class="i-ri-download-2-line icon-xl" />
				<span class="i-ri-upload-2-line icon-xl" />
			</span>
		</svws-ui-header>
		<div class="page page-flex-row">
			<!-- Auswahl des Einrichtungsschrittes (linke Seite) -->
			<div class="h-full min-w-64 w-64 max-w-64 flex flex-col gap-2 m-2">
				<template v-if="enmDaten !== null">
					<svws-ui-button id="contentFocusField" :type="((lehrerEmailProbleme > 0) ? 'danger' : ((aktuell === 'creds') ? 'primary' : 'secondary'))" @click="onSelect('creds')" style="justify-content: normal">
						<div class="flex flex-col gap-1">
							<p class="text-left font-bold ">Zugangsdaten verwalten</p>
							<p class="text-left font-normal">
								<span v-if="lehrerEmailProbleme === 0">Ok</span>
								<svws-ui-tooltip v-else>
									{{ lehrerEmailProbleme }} Fehler bei den Dienst-Email-Adressen
									<template #content>
										<ul>
											<li v-if="lehrerOhneEmail > 1">{{ lehrerOhneEmail }} fehlende Adressen</li>
											<li v-if="lehrerOhneEmail === 1">{{ lehrerOhneEmail }} fehlende Adresse</li>
											<li v-if="lehrerDoppelteEmail > 1">{{ lehrerDoppelteEmail }} Duplikate</li>
											<li v-if="lehrerDoppelteEmail === 1">{{ lehrerDoppelteEmail }} Duplikat</li>
											<li v-if="lehrerFehlerhafteEmail > 1">{{ lehrerFehlerhafteEmail }} fehlerhafte Adressen</li>
											<li v-if="lehrerFehlerhafteEmail === 1">{{ lehrerFehlerhafteEmail }} fehlerhafte Adresse</li>
										</ul>
									</template>
								</svws-ui-tooltip>
							</p>
						</div>
					</svws-ui-button>
				</template>
				<svws-ui-button id="contentFocusField" :type="(aktuell === 'setup') ? 'primary' : 'secondary'" @click="onSelect('setup')" style="justify-content: normal">
					<div class="flex flex-col gap-1">
						<p class="text-left font-bold ">Verbindungsdaten einrichten</p>
						<p class="text-left font-normal">{{ hasConnectionInfo ? 'vorhanden' : 'keine vorhanden' }}{{ hasConnectionInfo ? (connected ? ', verbunden' : ', nicht verbunden') : '' }}</p>
					</div>
				</svws-ui-button>
				<template v-if="connected">
					<svws-ui-button id="contentFocusField" :type="(aktuell === 'synchronize') ? 'primary' : 'secondary'" @click="onSelect('synchronize')" style="justify-content: normal">
						<div class="flex flex-col gap-1">
							<p class="text-left font-bold ">Synchronisieren</p>
							<p class="text-left font-normal">Abgleich der Daten mit dem Webnotenmanager</p>
						</div>
					</svws-ui-button>
					<svws-ui-button :type="(aktuell === 'reset') ? 'primary' : 'secondary'" @click="onSelect('reset')" style="justify-content: normal">
						<div class="flex flex-col gap-1">
							<p class="text-left font-bold ">Zurücksetzen</p>
							<p class="text-left font-normal">Daten auf dem Webnotenmanager entfernen</p>
						</div>
					</svws-ui-button>
					<svws-ui-button :type="(aktuell === 'configure') ? 'primary' : 'secondary'" @click="onSelect('configure')" style="justify-content: normal">
						<div class="flex flex-col gap-1">
							<p class="text-left font-bold ">Einstellungen</p>
							<p class="text-left font-normal">Den Webnotenmanager konfigurieren</p>
						</div>
					</svws-ui-button>
				</template>
			</div>

			<!-- Spezielle Ansicht nach Auswahl des Einrichtungsschrittes -->
			<div class="min-w-fit flex flex-col gap-8">
				<!-- Die Lehrer-Credentials -->
				<div v-if="(aktuell === 'creds') && (enmDaten !== null)" class="h-full w-full overflow-hidden max-w-196">
					<enm-lehrer-credentials :enm-daten="() => enmDaten() ?? new ENMDaten()" :map-enm-initial-kennwoerter />
				</div>

				<!-- Den Webnotenmanager einrichten -->
				<div v-if="aktuell === 'setup'" class="max-w-196">
					<div class="text-headline-md mb-4">Verbindung zum Webnotenmanager einrichten</div>
					<template v-if="!hasConnectionInfo">
						<svws-ui-input-wrapper>
							<svws-ui-text-input class="contentFocusField" v-model.trim="url" type="text" placeholder="URL" />
							<svws-ui-text-input v-model.trim="token" type="text" placeholder="Secret" />
							<svws-ui-button type="primary" @click="updateCredentials" :disabled="(url === '') || (token === '')">
								Speichern
							</svws-ui-button>
						</svws-ui-input-wrapper>
					</template>
					<template v-else>
						<svws-ui-input-wrapper>
							<div>Adresse: {{ url }}</div>
							<svws-ui-button type="primary" @click="removeVerbindungsdaten">
								Verbindungsdaten entfernen
							</svws-ui-button>
							<svws-ui-button type="primary" @click="tryConnect(connectionInfo()!)">
								Verbindungsdaten prüfen
							</svws-ui-button>
						</svws-ui-input-wrapper>
						<svws-ui-input-wrapper v-if="connInfo!.tlsCertIsKnown === false" class="mt-8">
							<div class="text-headline-md">TLS-Zertifikat des Servers </div>
							<div v-if="cert === null">Kein Zertifikat angegeben.</div>
							<div v-else>
								<div class="text-headline-sm">Inhaber</div>
								<div class="pl-4">{{ cert.subject }}</div>
								<div class="text-headline-sm">Aussteller</div>
								<div class="pl-4">{{ cert.issuer }}</div>
								<div class="text-headline-sm">Gültigkeit</div>
								<div class="pl-4">von: {{ cert.validSince }}</div>
								<div class="pl-4">bis: {{ cert.validUntil }}</div>
							</div>
							<svws-ui-checkbox :model-value="connInfo!.tlsCertIsTrusted" @update:model-value="value => trustCertificate(value)">
								Zertifikat vertrauen?
							</svws-ui-checkbox>
						</svws-ui-input-wrapper>
					</template>
				</div>

				<!-- Daten mit dem Webnotenmanager synchronisieren -->
				<div v-if="aktuell === 'synchronize'" class="max-w-164">
					<div class="text-headline-md mb-4">Daten abgleichen</div>
					<svws-ui-input-wrapper>
						<div>
							<div> Führt einen Abgleich der Daten in beide Richtungen durch, indem zuerst die neuen lokalen Daten zum Webnotenmanager hochgeladen werden und anschließend neue Daten vom Webnotenmanager abgeholt werden. </div>
							<svws-ui-button type="primary" @click="call(synchronize)">
								<span class="i-ri-download-2-line icon" />
								<span class="i-ri-upload-2-line icon mr-2" />
								Synchronisieren
							</svws-ui-button>
						</div>
						<div>
							<div> Lädt die lokalen Daten zum Webnotenmanager hoch und aktualisiert diesen ggf. mit neueren Daten dort. </div>
							<svws-ui-button type="primary" @click="call(upload)">
								<span class="i-ri-upload-2-line icon mr-2" />
								Hochladen
							</svws-ui-button>
						</div>
						<div>
							<div> Lädt die Daten vom Webnotenmanager herunter und aktualisiert ggf. die lokalen Daten.</div>
							<svws-ui-button type="primary" @click="call(download)">
								<span class="i-ri-download-2-line icon mr-2" />
								Herunterladen
							</svws-ui-button>
						</div>
					</svws-ui-input-wrapper>
				</div>

				<!-- Den Webnotenmanager zurücksetzen -->
				<div v-if="aktuell === 'reset'" class="max-w-164">
					<div class="text-headline-md mb-4">Zurücksetzen der Daten</div>
					<svws-ui-input-wrapper>
						<div>
							<div> Entfernt alle Daten des Lernabschnittes vom Webnotenmanager. Die Benutzerdaten bleiben erhalten.</div>
							<svws-ui-button type="primary" @click="call(reset)">
								Daten zurücksetzen
							</svws-ui-button>
						</div>
						<div>
							<div> Entfernt alle Daten des Lernabschnittes vom Webnotenmanager und auch die Benutzerdaten. Die dort gespeicherten Anmeldeinformationen gehen damit verloren.</div>
							<svws-ui-button type="danger" @click="call(truncate)">
								Daten und Benutzer zurücksetzen
							</svws-ui-button>
						</div>
					</svws-ui-input-wrapper>
				</div>

				<!-- Den Webnotenmanager konfigurieren -->
				<div v-if="aktuell === 'configure'" class="max-w-164">
					<div class="text-headline-md mb-4">Webnotenmanager konfigurieren</div>
					<svws-ui-input-wrapper class="mt-8">
						<div class="text-headline-md">SMTP Einstellungen</div>
						<svws-ui-text-input v-model.trim="smtpConfig.host" placeholder="SMTP-Server" />
						<svws-ui-input-number v-model="smtpConfig.port" placeholder="Port" :min="1" />
						<svws-ui-text-input v-model.trim="smtpConfig.username" placeholder="Benutzername" />
						<svws-ui-text-input v-model.trim="smtpConfig.password" placeholder="Passwort" type="password" />
						<svws-ui-checkbox v-model="smtpConfig.useTLS">TLS verwenden</svws-ui-checkbox>
						<svws-ui-text-input v-model.trim="smtpConfig.fromEmail" placeholder="Absenderadresse der Email" />
						<svws-ui-text-input v-model.trim="smtpConfig.fromName" placeholder="Absendername der Email" />
						<svws-ui-button type="primary" @click="updateSMTP">
							Speichern
						</svws-ui-button>
					</svws-ui-input-wrapper>
				</div>
				<div>
					<svws-ui-spinner :spinning />
				</div>
			</div>

			<!-- Die Ausgabe des Logs -->
			<div v-if="status !== null" class="min-w-fit grow h-full overflow-hidden flex flex-col gap-4">
				<log-box :logs="status.log" :status="status.success" />
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, onMounted, ref } from "vue";
	import type { SchuleDatenaustauschWenomProps } from './SSchuleDatenaustauschWenomProps';
	import type { OAuth2ClientConnection, SimpleOperationResponse, TLSCertificate } from "@core";
	import { ENMDaten, ENMServerConfigElement, ENMServerConfigSMTP, JavaString } from "@core";

	const props = defineProps<SchuleDatenaustauschWenomProps>();

	type WENOM = 'creds' | 'setup' | 'reset' | 'synchronize' | 'configure';
	const aktuell = ref<WENOM>('setup');

	const lehrerOhneEmail = ref<number>(0);
	const lehrerDoppelteEmail = ref<number>(0);
	const lehrerFehlerhafteEmail = ref<number>(0);
	const lehrerEmailProbleme = computed<number>(() => lehrerOhneEmail.value + lehrerDoppelteEmail.value + lehrerFehlerhafteEmail.value);

	onMounted(async () => {
		checkENMLehrerEMailAdressen();
		status.value = null;
		spinning.value = true;
		const connInfo = props.connectionInfo();
		url.value = connInfo?.authServer ?? ''; // Die URL soll zu Beginn geladen werden
		if (connInfo !== null)
			await tryConnect(connInfo);
		spinning.value = false;
	});

	const hasConnectionInfo = computed<boolean>(() => {
		const connInfo = props.connectionInfo();
		return (connInfo !== null) && (!JavaString.isBlank(connInfo.authServer)) && (!JavaString.isBlank(connInfo.clientSecret));
	});

	const connInfo = computed<OAuth2ClientConnection | null>(() => props.connectionInfo());

	async function updateCredentials() {
		const connInfo = props.connectionInfo();
		if (connInfo === null)
			return;
		status.value = null;
		spinning.value = true;
		const success = await props.setCredentials(url.value, token.value);
		if (success) {
			connInfo.authServer = url.value;
			connInfo.clientSecret = token.value;
			token.value = ''; // das Token soll nach dem Einlesen oder Setzen nicht mehr sichtbar sein
			await tryConnect(connInfo);
		}
		spinning.value = false;
	}

	async function tryConnect(connInfo : OAuth2ClientConnection) {
		if (JavaString.isBlank(connInfo.authServer) || JavaString.isBlank(connInfo.clientSecret))
			return;
		const res = await props.connect();
		if (res.success)
			aktuell.value = 'synchronize';
		else
			status.value = res;
	}

	const cert = computed<TLSCertificate | null>(() => {
		const connInfo = props.connectionInfo();
		if ((connInfo === null) || (connInfo.tlsCertChain.size() < 1))
			return null;
		return connInfo.tlsCertChain.getFirst();
	});

	const smtpConfig = computed(() => {
		const json = props.serverConfig().get('smtp');
		if (json !== null)
			return ENMServerConfigSMTP.transpilerFromJSON(json);
		return new ENMServerConfigSMTP();
	});

	const url = ref<string>("");
	const token = ref<string>("");

	const status = ref<SimpleOperationResponse | null>(null);
	const spinning = ref<boolean>(false);

	const validatorEmail = (value: string | null) : boolean => ((value === null) || (value === '')) ? true : (
		/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))[^@]?$/.test(value) ||
		/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(value)
	);

	function onSelect(value : WENOM): void {
		if (aktuell.value === value)
			return;
		aktuell.value = value;
		status.value = null;
	}

	async function call(func: () => Promise<SimpleOperationResponse>) {
		status.value = null;
		spinning.value = true;
		status.value = await func();
		spinning.value = false;
	}

	async function updateSMTP() {
		status.value = null;
		spinning.value = true;
		const element = new ENMServerConfigElement();
		element.key = "smtp";
		element.value = ENMServerConfigSMTP.transpilerToJSON(smtpConfig.value);
		element.type = "server";
		await props.setServerConfigElement(element);
		spinning.value = false;
	}

	async function removeVerbindungsdaten() {
		status.value = null;
		spinning.value = true;
		await props.removeCredentials();
		token.value = '';
		spinning.value = false;
	}

	function checkENMLehrerEMailAdressen() {
		const enmDaten = props.enmDaten();
		if (enmDaten !== null) {
			let emailFehlt = 0;
			let emailDoppelt = 0;
			let emailFehlerhaft = 0;
			const adressen = new Set<string>();
			for (const lehrer of enmDaten.lehrer) {
				if ((lehrer.eMailDienstlich === null) || (lehrer.eMailDienstlich.trim().length === 0)) {
					emailFehlt++;
					continue;
				}
				if (adressen.has(lehrer.eMailDienstlich)) {
					emailDoppelt++;
					continue;
				}
				adressen.add(lehrer.eMailDienstlich);
				if (!validatorEmail(lehrer.eMailDienstlich))
					emailFehlerhaft++;
			}
			lehrerOhneEmail.value = emailFehlt;
			lehrerDoppelteEmail.value = emailDoppelt;
			lehrerFehlerhafteEmail.value = emailFehlerhaft;
		} else {
			lehrerOhneEmail.value = 0;
			lehrerDoppelteEmail.value = 0;
			lehrerFehlerhafteEmail.value = 0;
		}
	}

</script>
