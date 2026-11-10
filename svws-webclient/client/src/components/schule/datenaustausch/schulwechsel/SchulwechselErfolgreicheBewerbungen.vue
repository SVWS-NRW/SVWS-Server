<template>
	<div class="page flex flex-col gap-4 overflow-y-auto">
		<div class="flex items-center gap-4">
			<svws-ui-button :disabled="isImportRunning || state.isPreviewRunning" @click="runImport()">Daten importieren</svws-ui-button>
			<!-- Während der Abfrage beim Remote-Server wird ein Platzhalter angezeigt. Ist die Anzahl unbekannt (Fehler), erscheint keine Aussage. -->
			<output v-if="state.isPreviewRunning" aria-label="Prüfe auf neue Daten" class="inline-block h-[1em] w-52 rounded-sm animate-pulse bg-ui-75" />
			<span v-else-if="newDataText !== null">{{ newDataText }}</span>
			<span v-else-if="state.newBewerbungenCount === 0" class="text-ui-50">Keine neuen Daten.</span>
		</div>
		<svws-ui-table :columns :items="state.erfolgreicheBewerbungen" v-model="selection" selectable count
			:no-data="state.erfolgreicheBewerbungen.isEmpty()" no-data-text="Es liegen keine erfolgreichen Bewerbungen vor.">
			<template #cell(idStatus)="{ value }">
				{{ getStatusLabel(value) }}
			</template>
			<template #cell(lastModified)="{ value }">
				{{ formatLastModified(value) }}
			</template>
			<template #actions>
				<!-- Noch ohne Funktion -->
				<svws-ui-button disabled>Datensätze anlegen</svws-ui-button>
			</template>
		</svws-ui-table>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";

	import type { SchulwechselErfolgreicheBewerbung } from "@core/core/data/schule/SchulwechselErfolgreicheBewerbung";
	import { StatusSchulwechselErfolgreicheBewerbung } from "@core/core/types/schule/StatusSchulwechselErfolgreicheBewerbung";
	import { useNotificationsState } from "@ui/states/NotificationsState";
	import { useSchulwechselErfolgreicheBewerbungenState } from "@ui/states/schulwechsel/SchulwechselErfolgreicheBewerbungenState";
	import type { DataTableColumn } from "@ui/types";

	const state = useSchulwechselErfolgreicheBewerbungenState();
	const notificationsState = useNotificationsState();

	const columns: DataTableColumn[] = [
		{ key: "idStatus", label: "Status" },
		{ key: "lastModified", label: "Letzte Änderung" },
		{ key: "idSchulkindSchulbewerbung", label: "UUID Schulbewerbung" },
	];

	const selection = ref<SchulwechselErfolgreicheBewerbung[]>([]);
	const isImportRunning = ref<boolean>(false);

	/** Der Text zu neuen Bewerbungen beim Remote-Server oder null, wenn keine vorliegen oder die Anzahl unbekannt ist */
	const newDataText = computed<string | null>(() => {
		const count = state.newBewerbungenCount;
		if ((count === null) || count <= 0) {
			return null;
		}
		return (count === 1) ? "Es liegt 1 neuer Datensatz zum Import bereit." : `Es liegen ${count} neue Datensätze zum Import bereit.`;
	});

	// Zusätzlich zum Text neben dem Button wird ein Hinweis eingeblendet, der nach einigen Sekunden automatisch verschwindet.
	// Der Watcher endet mit dem Verlassen der Ansicht, sodass eine spät eintreffende Antwort keinen Hinweis auf einer anderen Seite auslöst.
	watch(newDataText, (text) => {
		if (text !== null) {
			notificationsState.info("Neue Daten vorhanden", text);
		}
	}, { immediate: true });

	function getStatusLabel(idStatus: number): string {
		return StatusSchulwechselErfolgreicheBewerbung.getByIdOrNull(idStatus)?.getBezeichnung() ?? "";
	}

	/**
	 * Der Server liefert den Zeitpunkt als UTC im Format "yyyy-MM-dd HH:mm:ss".
	 * Die Anzeige erfolgt in der lokalen Zeitzone. Lässt sich der Wert nicht lesen, wird er unverändert angezeigt.
	 */
	function formatLastModified(lastModified: string): string {
		const zeitpunkt = new Date(lastModified.replace(" ", "T") + "Z");
		if (Number.isNaN(zeitpunkt.getTime())) {
			return lastModified;
		}
		return zeitpunkt.toLocaleString("de-DE", { day: "2-digit", month: "2-digit", year: "numeric", hour: "2-digit", minute: "2-digit" });
	}

	async function runImport(): Promise<void> {
		isImportRunning.value = true;
		try {
			const importedCount = await state.sync();
			if (importedCount === null) {
				return;
			}
			selection.value = [];
			// Ohne neue Datensätze ist der Import kein Erfolg im eigentlichen Sinne, sondern nur ein Hinweis
			if (importedCount === 0) {
				notificationsState.info("Import abgeschlossen", "Es lagen keine neuen Datensätze zum Import vor.");
				return;
			}
			notificationsState.success("Import abgeschlossen", (importedCount === 1) ? "1 neuer Datensatz wurde importiert." : `${importedCount} neue Datensätze wurden importiert.`);
		} finally {
			isImportRunning.value = false;
		}
	}

</script>
