<template>
	<div class="flex flex-col w-full h-full overflow-hidden">
		<header class="svws-ui-header max-w-560">
			<div class="svws-ui-header--title gap-x-8 lg:gap-x-16 w-full">
				<div class="svws-headline-wrapper flex-2">
					<h2 class="svws-headline">
						<span>Konfiguration des SVWS-Servers</span>
					</h2>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<div class="page page-grid-cards">
			<svws-ui-content-card title="Zertifikate">
				<svws-ui-spacing :size="2" />
				<div class="font-bold">Das im Keystore verwendete Zertifikat herunterladen</div>
				<svws-ui-button type="primary" @click="downloadZertifikat"><span class="icon-sm i-ri-download-2-line" /> Zertifikat exportieren</svws-ui-button>
				<svws-ui-spacing :size="2" />
				<div class="font-bold">Ein selbstsigniertes Zertifikat erstellen und im Keystore verwenden</div>
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input v-model="alias" placeholder="Alias" required />
					<svws-ui-text-input v-model="countryName" placeholder="Land" required />
					<svws-ui-text-input v-model="stateOrProvinceName" placeholder="Bundesland" required />
					<svws-ui-text-input v-model="localityName" placeholder="Stadt" required />
					<svws-ui-text-input v-model="organizationName" placeholder="Schule" required />
					<svws-ui-text-input v-model="commonName" placeholder="Domain" required />
					<svws-ui-text-input v-model="ips" placeholder="IPs" />Geben Sie eine Liste von IPs an, getrennt durch Kommas, z.B.: 10.1.0.1, 10.2.2.3, 10.3.2.1
					<svws-ui-text-input v-model="dns" placeholder="DNS" />Geben Sie eine Liste von DNS an, getrennt durch Kommas, z.B. beispiel.de, example.de
				</svws-ui-input-wrapper>
				<svws-ui-button type="primary" @click="generateZertifikat" :disabled><span class="icon-sm i-ri-certificate-2-line" /> Zertifikat generieren <svws-ui-spinner :spinning="apiStatus.pending" /></svws-ui-button>
				<svws-ui-spacing :size="2" />
				<svws-ui-notification v-if="createOk !== null" :type="createOk ? 'success' : 'warning'">
					{{ createOk ? "Das Zertifikat wurde erfolgreich erstellt" : "Das Zertifikat konnte nicht erstellt werden" }}
				</svws-ui-notification>
				<svws-ui-spacing :size="2" />
				<div class="font-bold">Ein Zertifikat importieren</div>
				<div>Verwenden Sie zum Hochladen jeweils eine Datei im Base64-Format für den Private Key und für das Zertifikat</div>
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input v-model="aliasUpload" placeholder="Alias" required />
					<div />
					<input class="my-2" type="file" @change="e => onFileChanged(e, 'key')" :disabled="loadingKey" accept=".key">
					<div>Private Key (.key) hochladen</div>
					<input class="my-2" type="file" @change="e => onFileChanged(e, 'crt')" :disabled="loadingCrt" accept=".crt">
					<div>Zertifikat (.crt) hochladen</div>
				</svws-ui-input-wrapper>
				<svws-ui-button type="primary" @click="uploadZertifikat" :disabled="disabledUpload"><span class="icon-sm i-ri-upload-2-line" /> Zertifikat importieren <svws-ui-spinner :spinning="apiStatus.pending" /></svws-ui-button>
				<svws-ui-spacing :size="2" />
				<svws-ui-notification v-if="uploadOk !== null" :type="uploadOk ? 'success' : 'warning'">
					{{ uploadOk ? "Das Zertifikat wurde erfolgreich importiert" : "Das Zertifikat konnte nicht importiert werden" }}
				</svws-ui-notification>
			</svws-ui-content-card>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { ConfigAppProps } from "./SConfigAppProps";
	import { TLSCertificateInfo } from "@core/core/data/TLSCertificateInfo";
	import { ArrayList } from "@core/java/util/ArrayList";

	const props = defineProps<ConfigAppProps>();

	const alias = ref<string>("default");
	const countryName = ref<string>("de");
	const stateOrProvinceName = ref<string>("NRW");
	const localityName = ref<string>("");
	const organizationName = ref<string>("");
	const commonName = ref<string>("");
	const ips = ref<string>("");
	const dns = ref<string>("");
	const createOk = ref<boolean | null>(null);

	function generateDn() {
		const arrDn: string[] = [];
		if (countryName.value.trim().length > 0) {
			arrDn.push(`C=${countryName.value.trim()}`);
		}
		if (stateOrProvinceName.value.trim().length > 0) {
			arrDn.push(`ST=${stateOrProvinceName.value.trim()}`);
		}
		if (localityName.value.trim().length > 0) {
			arrDn.push(`L=${localityName.value.trim()}`);
		}
		if (organizationName.value.trim().length > 0) {
			arrDn.push(`O=${organizationName.value.trim()}`);
		}
		if (commonName.value.trim().length > 0) {
			arrDn.push(`CN=${commonName.value.trim()}`);
		}
		return arrDn.join(',');
	}

	function generateSans() {
		const sans = new ArrayList<string>();
		const arrIps = ips.value.split(",");
		const arrDns = dns.value.split(",");
		for (const ip of arrIps) {
			const trim = ip.trim();
			if (trim.length > 0) {
				sans.add(`IP:${trim}`);
			}
		}
		for (const dns of arrDns) {
			const trim = dns.trim();
			if (trim.length > 0) {
				sans.add(`DNS:${trim}`);
			}
		}
		return sans;
	}

	const disabled = computed(() => (generateDn().length === 0) || generateSans().isEmpty() || props.apiStatus.pending);

	async function generateZertifikat() {
		createOk.value = null;
		const tlsCertificateInfo = new TLSCertificateInfo();
		tlsCertificateInfo.dn = generateDn();
		tlsCertificateInfo.sans.addAll(generateSans());
		createOk.value = await props.createCert(tlsCertificateInfo, alias.value);
	}

	async function downloadZertifikat() {
		const { data, name } = await props.getCert();
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

	const aliasUpload = ref<string>("default");
	const fileKey = ref<File | null>(null);
	const fileCrt = ref<File | null>(null);

	const loadingKey = ref<boolean>(false);
	const loadingCrt = ref<boolean>(false);

	function onFileChanged(event: Event, type: 'key' | 'crt') {
		uploadOk.value = null;
		const target = event.target as HTMLInputElement;
		if (target.files) {
			if (type === 'key') {
				loadingKey.value = true;
				fileKey.value = target.files[0];
				loadingKey.value = false;
			} else {
				loadingCrt.value = true;
				fileCrt.value = target.files[0];
				loadingCrt.value = false;
			}
		}
	}

	const uploadOk = ref<boolean | null>(null);
	const disabledUpload = computed(() => (fileCrt.value === null) || (fileKey.value === null) || props.apiStatus.pending);

	async function uploadZertifikat() {
		if ((fileKey.value === null) || (fileCrt.value === null)) {
			return;
		}
		const formData = new FormData();
		formData.append("key", fileKey.value);
		formData.append('certificate', fileCrt.value);
		uploadOk.value = await props.uploadCert(formData, aliasUpload.value);
	}

</script>
