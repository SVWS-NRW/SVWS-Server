<template>
	<svws-ui-table clickable :clicked="auswahlBlockung" @update:clicked="select_blockungauswahl" :items="listBlockungen" :columns="[{ key: 'name', label: 'Blockungen' }]" no-data-text="Es liegt noch keine Planung für dieses Halbjahr vor.">
		<template #noData v-if="istBlockungPersistiert">
			<span class="inline-flex gap-1 leading-tight">
				<span class="icon-sm icon-error i-ri-error-warning-line flex-shrink-0" />
				<span>Es liegt bereits eine persistierte Blockung vor, die wiederhergestellt werden kann.</span>
			</span>
		</template>
		<template #header(name)>
			<span>Blockungen</span>
		</template>
		<template #cell(name)="{ rowData: blockung }">
			<div class="flex justify-between w-full items-start">
				<div class="flex items-center gap-1 w-full">
					<div class="flex" v-if="blockung === auswahlBlockung">
						<span v-if="!edit_blockungsname" class="border-b border-dotted hover:border-transparent cursor-text line-clamp-1 break-all leading-tight -my-0.5" @click.stop="hatUpdateKompetenz && (edit_blockungsname = true)">
							{{ blockung.name }}
						</span>
						<svws-ui-text-input v-else :model-value="blockung.name" headless focus
							@keyup.enter="patch_blockung($event.target.value, blockung.id)" @keyup.escape="edit_blockungsname = false"
							@change="name => name && patch_blockung(name, blockung.id)" class="-my-0.5 w-full" />
					</div>
					<div v-else>
						<span>{{ blockung.name }}&nbsp;</span>
					</div>
					<div class="-my-1 ml-auto inline-flex gap-1">
						<template v-if="hatUpdateKompetenz && ((auswahlBlockung !== undefined) && !isPending(auswahlBlockung.id)) && (blockung === auswahlBlockung)">
							<template v-if="allow_berechne_blockung">
								<s-gost-kursplanung-modal-blockung-ausfuehrlich-berechnen v-if="allow_berechne_blockung" :map-core-type-name-json-data :ausfuehrliche-darstellung-kursdifferenz :set-ausfuehrliche-darstellung-kursdifferenz :get-datenmanager :add-ergebnisse v-slot="{ openModal }">
									<svws-ui-button type="transparent" @click="openModal" title="Ausführliche Berechnung lokal im Browser und Auswahl von guten Ergebnissen" :disabled="apiStatus.pending" class="text-black dark:text-white mr-4">
										<span class="icon-sm i-ri-calculator-line -mx-0.5" /> Blocken…
									</svws-ui-button>
								</s-gost-kursplanung-modal-blockung-ausfuehrlich-berechnen>
								<!-- <svws-ui-button type="transparent" @click.stop="do_create_blockungsergebnisse" title="Schnelle Berechnung auf dem Server mit direkter Übernahme der Ergebnisse" :disabled="apiStatus.pending" v-if="allow_berechne_blockung" class="text-black dark:text-white"> <span class="icon-sm i-ri-calculator-line -mx-0.5" /> Schnell </svws-ui-button> -->
							</template>
							<svws-ui-tooltip position="top" v-else>
								<svws-ui-button type="transparent" disabled> <span class="icon-sm i-ri-calculator-line -mx-0.5" />Blocken…</svws-ui-button>
								<template #content>
									<div class="normal-case text-base rich-text">
										Damit Kursblockungen berechnet werden können, müssen zumindest Fachwahlen, Fächer und Kurse existieren.
									</div>
								</template>
							</svws-ui-tooltip>
							<s-gost-kursplanung-remove-blockung-modal :remove-blockung v-slot="{ openModal }">
								<svws-ui-button type="icon" @click.stop="openModal" title="Blockung löschen" :disabled="apiStatus.pending" class="text-black dark:text-white">
									<span class="icon-sm i-ri-delete-bin-line -mx-0.5" />
								</svws-ui-button>
							</s-gost-kursplanung-remove-blockung-modal>
						</template>
						<template v-if="hatUpdateKompetenz">
							<svws-ui-tooltip v-if="blockung.istAktiv">
								<span class="icon icon-primary i-ri-checkbox-circle-fill ml-2 hover:opacity-50" @click="patchBlockung({ istAktiv: false }, blockung.id)" />
								<template #content> Aktivierte Blockung </template>
							</svws-ui-tooltip>
							<svws-ui-tooltip v-else>
								<span class="icon icon-primary i-ri-checkbox-circle-line ml-2 opacity-25 hover:opacity-75" @click="patchBlockung({ istAktiv: true }, blockung.id)" />
								<template #content> Blockung als aktiv markieren </template>
							</svws-ui-tooltip>
						</template>
						<template v-else>
							<span v-if="blockung.istAktiv" class="icon icon-primary i-ri-checkbox-circle-fill ml-2" />
							<span v-else class="icon icon-primary i-ri-checkbox-circle-line ml-2 opacity-25" />
						</template>
					</div>
				</div>
			</div>
		</template>
		<template #actions v-if="hatUpdateKompetenz">
			<svws-ui-button type="icon" title="Neue Blockung hinzufügen" @click.stop="addBlockung">
				<span class="icon-sm i-ri-add-line -mx-0.5" />
			</svws-ui-button>
		</template>
	</svws-ui-table>
	<div v-if="(auswahlBlockung !== undefined) && isPending(auswahlBlockung.id)" class="my-3 flex gap-1 items-center mb-5 px-7 3xl:px-8" :class="{'animate-pulse': !apiStatus.hasError}">
		<template v-if="apiStatus.pending">
			<svws-ui-spinner spinning />
			<span class="text-button text-black/50 dark:text-white/50">Ergebnisse werden berechnet…</span>
		</template>
		<template v-if="apiStatus.hasError">
			<span class="text-error font-bold">Fehler beim Berechnen der Blockung.</span>
		</template>
	</div>
	<s-gost-kursplanung-ergebnis-auswahl v-if="hatBlockung" :halbjahr :api-status :get-datenmanager :patch-ergebnis :remove-ergebnisse :goto-ergebnis :auswahl-ergebnis :hat-update-kompetenz />
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { ApiStatus } from '~/components/ApiStatus';
	import type { ServerMode, GostBlockungListeneintrag, GostBlockungsdaten, GostBlockungsdatenManager, GostBlockungsergebnis, GostHalbjahr, List, GostBlockungsergebnisManager} from "@core";
	import { ArrayList, BlockungsUtils } from "@core";

	const props = defineProps<{
		addBlockung: () => Promise<void>;
		patchBlockung: (data: Partial<GostBlockungsdaten>, idBlockung: number) => Promise<boolean>;
		removeBlockung: () => Promise<void>;
		gotoBlockung: (auswahl: GostBlockungListeneintrag | undefined) => Promise<void>;
		auswahlBlockung: GostBlockungListeneintrag | undefined;
		mapBlockungen: () => Map<number, GostBlockungListeneintrag>;
		halbjahr: GostHalbjahr;
		apiStatus: ApiStatus;
		// ... zusätzlich für die Ergebnisauswahl
		getDatenmanager: () => GostBlockungsdatenManager;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		patchErgebnis: (data: Partial<GostBlockungsergebnis>, idErgebnis: number) => Promise<boolean>;
		rechneGostBlockung: () => Promise<List<number>>;
		addErgebnisse: (ergebnisse: List<GostBlockungsergebnis>) => Promise<void>;
		removeErgebnisse: (ergebnisse: GostBlockungsergebnis[]) => Promise<void>;
		gotoErgebnis: (value: GostBlockungsergebnis | undefined) => Promise<void>;
		hatBlockung: boolean;
		auswahlErgebnis: GostBlockungsergebnis | undefined;
		restoreBlockung: () => Promise<void>;
		istBlockungPersistiert: boolean;
		mode: ServerMode;
		ausfuehrlicheDarstellungKursdifferenz: () => boolean;
		setAusfuehrlicheDarstellungKursdifferenz: (value: boolean) => void;
		hatUpdateKompetenz: boolean;
		mapCoreTypeNameJsonData: () => Map<string, string>;
	}>();

	const edit_blockungsname = ref<boolean>(false);

	const allow_berechne_blockung = computed(() => (props.getDatenmanager().fachwahlGetAnzahl() > 0) && (props.getDatenmanager().getFaecherAnzahl() > 0) && (props.getDatenmanager().kursGetAnzahl() > 0));

	const listBlockungen = computed(() => {
		const list: List<GostBlockungListeneintrag> = new ArrayList();
		for (const i of props.mapBlockungen().values())
			list.add(i);
		BlockungsUtils.sortGostBlockungListeneintrag(list);
		return list;
	})

	async function select_blockungauswahl(blockung: GostBlockungListeneintrag | null) {
		if ((blockung === null) || props.apiStatus.pending)
			return;
		await props.gotoBlockung(blockung);
	}

	function isPending(id: number): boolean {
		return ((props.apiStatus.data !== undefined) && (props.apiStatus.data.name === "gost.kursblockung.berechnen") && (props.apiStatus.data.id === id));
	}

	async function do_create_blockungsergebnisse() {
		const id = props.auswahlBlockung?.id;
		if (id === undefined)
			return;
		await props.rechneGostBlockung();
	}

	async function patch_blockung(value: string, idBlockung : number) {
		const result = await props.patchBlockung({ name: value.toString() }, idBlockung);
		if (result && props.auswahlBlockung)
			props.auswahlBlockung.name = value.toString();
		edit_blockungsname.value = false;
	}

</script>
