<template>
	<svws-ui-header>
		<span class="inline-block mr-3">Datenaustausch mit dem Web-Notenmodul</span>
		<br>
		<span class="opacity-50 flex">
			<span class="i-ri-download-2-line icon-xl" />
			<span class="i-ri-upload-2-line icon-xl" />
		</span>
	</svws-ui-header>
	<div class="svws-ui-page w-full">
		<div class="svws-ui-tab-content">
			<div class="page--content page--content--full">
				<div v-if="secretSet()">
					<svws-ui-content-card title="Aktuelle Abschnittsdaten sychronisieren">
						<div class="flex items-start gap-3">
							<svws-ui-button type="primary" @click="wenomSynchronize">
								synchronisieren
							</svws-ui-button>
						</div>
					</svws-ui-content-card>
					<svws-ui-content-card title="Aktuelle Abschnittsdaten löschen">
						<div class="flex items-start gap-3">
							<svws-ui-button type="primary" @click="wenomTruncate">
								Abschnittsdaten löschen
							</svws-ui-button>
						</div>
					</svws-ui-content-card>
					<svws-ui-content-card title="Zugangsdaten löschen">
						<div class="flex items-start gap-3">
							<svws-ui-button type="primary" @click="wenomRemoveCredentials">
								Zugangsdaten löschen
							</svws-ui-button>
						</div>
					</svws-ui-content-card>
				</div>
				<div v-else>
					<svws-ui-content-card title="Zugangsdaten zum Webnotenmanager">
						<div class="flex items-start gap-3">
							<svws-ui-text-input id="contentFocusField" v-model.trim="url" type="text" placeholder="URL" />
							<svws-ui-text-input v-model.trim="token" type="text" placeholder="Secret" />
							<svws-ui-button type="primary" @click="setWenomCredentials(url, token)" :disabled="!url || !token">
								speichern
							</svws-ui-button>
						</div>
					</svws-ui-content-card>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { SchuleDatenaustauschWenomProps } from './SSchuleDatenaustauschWenomProps';

	const props = defineProps<SchuleDatenaustauschWenomProps>();


	const url = ref<string>("");
	const token = ref<string>("");

</script>
