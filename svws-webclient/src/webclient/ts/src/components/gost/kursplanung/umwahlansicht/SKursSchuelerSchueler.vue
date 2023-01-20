<template>
	<tr class="cursor-pointer" :class="{ 'selected': selected }">
		<td>
			<div class="flex justify-between items-center">
				<span>
					{{ `${schueler.nachname}, ${schueler.vorname}` }}
				</span>
				<div>
					<svws-ui-badge v-if="schueler.status !== 'Aktiv'" type="light" size="big"> {{ schueler.status }} </svws-ui-badge>
					<div class="leading-none overflow-hidden" style="margin-bottom: -0.1em;">
						<svws-ui-popover v-if="kollision && !nichtwahl">
							<template #trigger>
								<svws-ui-icon> <i-ri-alert-line class="text-error" /> </svws-ui-icon>
							</template>
							<template #content>
								<span>Kollision</span>
							</template>
						</svws-ui-popover>
						<svws-ui-popover v-if="!kollision && nichtwahl">
							<template #trigger>
								<svws-ui-icon> <i-ri-forbid-2-line class="text-dark-20" /> </svws-ui-icon>
							</template>
							<template #content>
								<span>Nichtwahl</span>
							</template>
						</svws-ui-popover>
						<svws-ui-popover v-if="kollision && nichtwahl">
							<template #trigger>
								<svws-ui-icon> <i-ri-alert-fill class="text-error" /> </svws-ui-icon>
							</template>
							<template #content>
								<span>Kollision und Nichtwahl</span>
							</template>
						</svws-ui-popover>
					</div>
				</div>
			</div>
		</td>
	</tr>
</template>

<script setup lang="ts">

	import { GostBlockungsergebnisManager, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { ComputedRef, computed } from "vue";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";
	import { ListAbiturjahrgangSchueler } from "~/apps/gost/ListAbiturjahrgangSchueler";

	const props = defineProps<{
		schueler: SchuelerListeEintrag;
		selected: Boolean;
		blockung: DataGostKursblockung;
		listSchueler: ListAbiturjahrgangSchueler;
	}>();

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> = computed(() => props.blockung.ergebnismanager);

	const kollision: ComputedRef<boolean> = computed(()=> {
		if (manager.value === undefined)
			return false;
		const kursid = props.listSchueler.filter.kurs?.id;
		if (kursid === undefined)
			return manager.value.getOfSchuelerHatKollision(props.schueler.id);
		return manager.value.getOfKursSchuelermengeMitKollisionen(kursid).contains(props.schueler.id);
	});

	const nichtwahl: ComputedRef<boolean> = computed(() =>
		(manager.value === undefined) ? false : manager.value.getOfSchuelerHatNichtwahl(props.schueler.id)
	);

</script>
