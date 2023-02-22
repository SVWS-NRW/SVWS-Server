<template>
	<svws-ui-secondary-menu>
		<template #headline>Abiturjahrgänge</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" />
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-table v-model="selected" :columns="cols" :data="rows" id="abiturjahr" :footer="true">
					<template #cell-abiturjahr="{ row }">
						{{ row.abiturjahr === -1 ? '' : row.abiturjahr }}
						<span v-if="(pending && row.abiturjahr === selected?.abiturjahr)" class="loading-spinner-dimensions">
							<img src="/loading_spinner.svg" alt="Ladeanzeige" class="loading-spinner-dimensions loading-rotation"></span>
					</template>
					<template #footer>
						<svws-ui-dropdown type="icon" class="">
							<template #dropdownButton>Abiturjahr hinzufügen</template>
							<template #dropdownItems>
								<svws-ui-dropdown-item v-for="jahrgang in mapJahrgaengeOhneAbiJahrgang.values()" :key="jahrgang.id" class="px-2" @click="abiturjahr_hinzufuegen(jahrgang)">
									{{ jahrgang.kuerzel }}
								</svws-ui-dropdown-item>
							</template>
						</svws-ui-dropdown>
					</template>
				</svws-ui-table>
				<router-view name="gost_child_auswahl" />
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { GostJahrgang, JahrgangsListeEintrag, List, Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { routeGost } from "~/router/apps/RouteGost";
	import { DataTableColumn } from "@svws-nrw/svws-ui";
	import { ApiStatus } from "~/router/ApiStatus";

	const props = defineProps<{
		addAbiturjahrgang: (idJahrgang: number) => Promise<void>;
		item: GostJahrgang | undefined;
		mapJahrgaengeOhneAbiJahrgang: Map<number, JahrgangsListeEintrag>;
		abschnitte: List<Schuljahresabschnitt>;
		aktAbschnitt: Schuljahresabschnitt;
		setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
		apiStatus: ApiStatus;
	}>();

	const selected: WritableComputedRef<GostJahrgang | undefined> = routeGost.auswahl;

	const cols: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 2 },
		{ key: "abiturjahr", label: "Abiturjahr", sortable: true },
		{ key: "jahrgang", label: "Stufe", sortable: true }];

	const rows: ComputedRef<GostJahrgang[]> = computed(() => {
		const list = [...routeGost.liste.liste];
		return list.sort((a, b) => (a?.bezeichnung || "") < (b?.bezeichnung || "") ? 1 : -1)
	});

	const pending: ComputedRef<boolean> = computed(() => props.apiStatus.pending);

	async function abiturjahr_hinzufuegen(jahrgang: JahrgangsListeEintrag) {
		await props.addAbiturjahrgang(jahrgang.id);
	}
</script>

<style lang="postcss">

	.loading-disclaimer {
		background-color: rgba(var(--svws-ui-color-dark-20), var(--tw-border-opacity));
		--tw-bg-opacity: 1;
		--tw-border-opacity: 1;
		border-width: 1px;
		padding: .25rem .75rem;
		line-height: 1.125;
	}

	.loading-spinner-dimensions {
		height: 1rem;
		width: 1rem;
	}

	.loading-display {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
	}

	.loading-rotation {
		display: block;
		position: relative;
		-webkit-animation: spin 6s steps(11, end) infinite;
		-moz-animation: spin 6s steps(11, end) infinite;
		animation: spin 6s steps(11, end) infinite;
	}

	.api-error-text {
		color: rgb(var(--svws-ui-color-error));
		font-weight: 700;
	}

	@-webkit-keyframes spin {
		0% {
			-webkit-transform: rotate(0deg);
		}

		100% {
			-webkit-transform: rotate(360deg);
		}
	}

	@-moz-keyframes spin {
		0% {
			-webkit-transform: rotate(0deg);
		}

		100% {
			-webkit-transform: rotate(360deg);
		}
	}

	@keyframes spin {
		0% {
			-webkit-transform: rotate(0deg);
		}

		100% {
			-webkit-transform: rotate(360deg);
		}
	}

</style>
