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
		<template #cell(name)="{ rowData: row }">
			<div class="flex justify-between w-full items-start">
				<div class="flex items-center gap-1 w-full">
					<div class="flex" v-if="row === auswahlBlockung">
						<span v-if="(!edit_blockungsname)" class="border-b border-dotted hover:border-transparent cursor-text line-clamp-1 break-all leading-tight -my-0.5" @click.stop="edit_blockungsname = true">
							{{ row.name }}
						</span>
						<svws-ui-text-input v-else :model-value="row.name" headless focus
							@keyup.enter="(e: any) => patch_blockung(e.target.value, row.id)" @keyup.escape="edit_blockungsname=false"
							@change="name => patch_blockung(name, row.id)" class="-my-0.5 w-full" />
					</div>
					<div v-else>
						<span>{{ row.name }}&nbsp;</span>
					</div>
					<div class="-my-1 ml-auto inline-flex">
						<template v-if="visible && (auswahlBlockung !== undefined && !isPending(auswahlBlockung.id)) && row === auswahlBlockung">
							<template v-if="allow_berechne_blockung">
								<s-gost-kursplanung-modal-blockung-ausfuehrlich-berechnen v-if="allow_berechne_blockung" :get-datenmanager="getDatenmanager" :add-ergebnisse="addErgebnisse" v-slot="{ openModal }">
									<svws-ui-button type="transparent" @click="openModal()" title="Ausführliche Berechnung lokal im Browser und Auswahl von guten Ergebnissen" :disabled="apiStatus.pending" class="text-black dark:text-white">
										<span class="icon-sm i-ri-calculator-line -mx-0.5" /> Ausführlich
									</svws-ui-button>
								</s-gost-kursplanung-modal-blockung-ausfuehrlich-berechnen>
								<svws-ui-button type="transparent" @click.stop="do_create_blockungsergebnisse" title="Schnelle Berechnung auf dem Server mit direkter Übernahme der Ergebnisse" :disabled="apiStatus.pending" v-if="allow_berechne_blockung" class="text-black dark:text-white"> <span class="icon i-ri-calculator-line -mx-0.5" /> Schnell </svws-ui-button>
							</template>
							<svws-ui-tooltip position="top" v-else>
								<svws-ui-button type="transparent" disabled> <span class="icon-sm i-ri-calculator-line -mx-0.5" />Berechnen</svws-ui-button>
								<template #content>
									<div class="normal-case text-base rich-text">
										Damit Kursblockungen berechnet werden können, müssen zumindest Fachwahlen, Fächer und Kurse existieren.
									</div>
								</template>
							</svws-ui-tooltip>
							<s-gost-kursplanung-remove-blockung-modal :remove-blockung="removeBlockung" v-slot="{ openModal }">
								<svws-ui-button type="icon" @click.stop="openModal()" title="Blockung löschen" :disabled="apiStatus.pending" class="text-black dark:text-white">
									<span class="icon-sm i-ri-delete-bin-line -mx-0.5" />
								</svws-ui-button>
							</s-gost-kursplanung-remove-blockung-modal>
						</template>
						<div v-else class="mr-2 ml-auto inline-flex">
							<svws-ui-tooltip v-if="row.anzahlErgebnisse > 1">
								<span class="icon-sm i-ri-lock-fill -mx-1" />
								<template #content>
									<span>
										{{ row.anzahlErgebnisse }} Ergebnisse vorhanden.
										<br>Es können nur bei den Blockungsergebnissen, d.h.
										den Kurs-Schienen- und der Kurs-Schülerzuordnungen, Änderungen vorgenommen werden.
										<br>Für weitere Anpassungen an der Blockung müssen entweder alle Ergebnisse bis auf eines gelöscht werden
										oder die Blockung muss zu einer neuen Blockung abgeleitet werden.
									</span>
								</template>
							</svws-ui-tooltip>
							<svws-ui-tooltip v-else autosize>
								<span class="icon-sm i-ri-lock-unlock-line -mx-1" />
								<template #content>
									<span>In dieser Blockung können Regeln erstellt werden.</span>
								</template>
							</svws-ui-tooltip>
						</div>
						<svws-ui-tooltip v-if="row.istAktiv">
							<span class="icon icon-primary i-ri-checkbox-circle-fill ml-2 hover:opacity-50" @click="patchBlockung({ istAktiv: false }, row.id)" />
							<template #content> Aktivierte Blockung </template>
						</svws-ui-tooltip>
						<svws-ui-tooltip v-else>
							<span class="icon icon-primary i-ri-checkbox-circle-line ml-2 opacity-25 hover:opacity-75" @click="patchBlockung({ istAktiv: true }, row.id)" />
							<template #content> Blockung als aktiv markieren </template>
						</svws-ui-tooltip>
					</div>
				</div>
			</div>
		</template>
		<template #actions>
			<slot name="blockungAuswahlActions" />
		</template>
	</svws-ui-table>
	<div v-if="auswahlBlockung !== undefined && isPending(auswahlBlockung.id)" class="my-3">
		<auswahl-blockung-api-status :blockung="auswahlBlockung" :api-status="apiStatus" />
	</div>
	<s-gost-kursplanung-ergebnis-auswahl v-if="hatBlockung" :halbjahr="halbjahr" :api-status="apiStatus"
		:get-datenmanager="getDatenmanager" :patch-ergebnis="patchErgebnis" :remove-ergebnisse="removeErgebnisse"
		:goto-ergebnis="gotoErgebnis" :auswahl-ergebnis="auswahlErgebnis" />
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { ApiStatus } from '~/components/ApiStatus';
	import type { ServerMode, GostBlockungListeneintrag, GostBlockungsdaten, GostBlockungsdatenManager, GostBlockungsergebnis, GostHalbjahr, List, GostBlockungsergebnisManager} from "@core";
	import { ArrayList, BlockungsUtils } from "@core";

	const props = defineProps<{
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
	}>();

	const edit_blockungsname = ref<boolean>(false);

	const allow_berechne_blockung = computed(()=>
		props.getDatenmanager().fachwahlGetAnzahl() > 0
		&& props.getDatenmanager().getFaecherAnzahl() > 0
		&& props.getDatenmanager().kursGetAnzahl() > 0
	)

	const listBlockungen = computed(()=> {
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

	const isPending = (id: number) : boolean => ((props.apiStatus.data !== undefined) && (props.apiStatus.data.name === "gost.kursblockung.berechnen") && (props.apiStatus.data.id === id));

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

	const visible = computed<boolean>(() => props.mapBlockungen().size > 0);

</script>
