<template>
	<div class="page page-grid-cards">
		<div>
			<div class="flex justify-between">
				<svws-ui-button :disabled="readonly" v-autofocus class="contentFocusField min-h-[32px]" @click="addVermerk">
					<span class="icon i-ri-chat-new-line" />
					<span class="ml-2">Neuen Vermerk hinzufügen</span>
				</svws-ui-button>
				<svws-ui-checkbox class="self-center" :model-value="filterNurSichtbare" @update:model-value="value => setFilterNurSichtbare(value)">Nur <span class="font-bold">sichtbare</span> Vermerkarten anzeigen</svws-ui-checkbox>
			</div>
			<div class="flex flex-col gap-4 mt-4">
				<ui-card v-for="vermerkModel of vermerkeModels" :key="vermerkModel.proxy.id"
					icon="i-ri-message-line"
					:title="getTitle(vermerkModel.proxy)"
					:subtitle="getDescription(vermerkModel.proxy)"
					:is-open="lastAddedVermerk?.id === vermerkModel.proxy.id">
					<svws-ui-input-wrapper class="px-6">
						<svws-ui-textarea-input placeholder="Bemerkung"
							v-model="vermerkModel.proxy.bemerkung"
							@change="vermerkModel.patch"
							:validation="() => vermerkModel.getFehler('bemerkung')"
							:readonly :rows="4" autoresize />
						<ui-select label="Vermerkart"
							v-model="vermerkModel.vermerkart.value"
							:manager="vermerkartenManager"
							:readonly />
						<div class="w-full flex justify-between">
							<div>
								<p class="text-headline-md my-1">{{ vermerkModel.proxy.angelegtVon }}</p>
								<svws-ui-spinner v-if="apiStatus.pending" class="min-h-8" :spinning="true" />
								<div v-else class="subTextContainer">
									<p v-if="vermerkModel.proxy.geaendertVon">
										Zuletzt bearbeitet von {{ vermerkModel.proxy.geaendertVon }} am
										{{ getDate(vermerkModel.proxy) }}
									</p>
									<p v-else>Erstellt am {{ getDate(vermerkModel.proxy) }}</p>
								</div>
							</div>
						</div>
					</svws-ui-input-wrapper>
					<template #buttonFooterRight>
						<svws-ui-button class="mt-4"
							title="Löschen"
							type="danger"
							@click="remove(vermerkModel.proxy.id)"
							:disabled="readonly">
							Löschen
						</svws-ui-button>
					</template>
				</ui-card>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref } from "vue";
	import type { SchuelerVermerkeProps } from "./SSchuelerVermerkeProps";
	import { SchuelerVermerkeModelProxy } from "./modelProxy/SchuelerVermerkeModelProxy";
	import type { SchuelerVermerke } from "@core/core/data/schueler/SchuelerVermerke";
	import type { VermerkartEintrag } from "@core/core/data/schule/VermerkartEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import { useModelProxyList } from "@ui/model/useModelProxyList";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";

	const props = defineProps<SchuelerVermerkeProps>();
	const benutzerState = useBenutzerState();

	const lastAddedVermerk = ref<SchuelerVermerke>();

	const hatKompetenzAendern = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_EINWILLIGUNGEN_AENDERN));
	const readonly = computed<boolean>(() => !hatKompetenzAendern.value);

	const filteredVermerke = computed<List<SchuelerVermerke>>(() => {
		if (!props.filterNurSichtbare) {
			return props.schuelerVermerke();
		}

		const filtered: List<SchuelerVermerke> = new ArrayList<SchuelerVermerke>();
		for (const item of props.schuelerVermerke()) {
			if (item.idVermerkart === null) {
				filtered.add(item);
				continue;
			}
			const art = props.mapVermerkArten.get(item.idVermerkart);
			if ((art !== undefined) && art.istSichtbar) {
				filtered.add(item);
			}
		}
		return filtered;
	});

	const vermerkarten = computed<Iterable<VermerkartEintrag>>(() => props.mapVermerkArten.values());

	const vermerkeModels = useModelProxyList(
		filteredVermerke,
		(vermerk) => vermerk.id,
		(vermerk) => new SchuelerVermerkeModelProxy(
			() => vermerk,
			() => props.mapVermerkArten,
			(data) => props.patch(data, vermerk.id)
		)
	);

	const vermerkartenManager = new SelectManager({
		options: vermerkarten,
		optionDisplayText: i => getItemText(i),
		selectionDisplayText: i => getItemText(i),
	});

	function getDate(vermerk: SchuelerVermerke) {
		return DateUtils.gibDatumGermanFormat(vermerk.datum ?? new Date().toISOString());
	}

	function getTitle(vermerk: SchuelerVermerke) {
		const title = `${props.mapVermerkArten.get(vermerk.idVermerkart ?? -1)?.bezeichnung ?? "Neuer Vermerk"}: ${vermerk.bemerkung ?? ""}`;
		return title.length > 50 ? title.substring(0, 50) + "..." : title;
	}

	function getItemText(item: VermerkartEintrag) {
		const vermerkArtIsVisible = !item.istSichtbar;
		return `${item.bezeichnung} ${vermerkArtIsVisible ? '(nicht sichtbar)' : ''}`;
	}

	function getDescription(vermerk: SchuelerVermerke): string {
		return `${vermerk.geaendertVon ?? vermerk.angelegtVon} - ${getDate(vermerk)}`;
	}

	async function addVermerk() {
		await props.add();
		lastAddedVermerk.value = props.schuelerVermerke().getFirst();
	}

</script>
