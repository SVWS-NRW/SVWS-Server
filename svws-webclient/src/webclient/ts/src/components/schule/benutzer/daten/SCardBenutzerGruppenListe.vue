<template>
	<svws-ui-content-card title="Benutzergruppen">
		<div class="flex flex-row gap-4 ">
			<table class="border-collapse text-sm ">
				<thead class="bg-slate-100">
					<tr>
						<td class="border border-[#7f7f7f]/20 text-center">
							<svws-ui-checkbox v-model="selected"> Alle </svws-ui-checkbox>
						</td>
					</tr>
				</thead>
				<tbody>
					<template v-for="bgle in benutzergruppen" :key="bgle.id">
						<s-benutzergruppen-listeneintrag :bgle="bgle" :data="data" />
					</template>
				</tbody>
			</table>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { BenutzergruppeListeEintrag, BenutzerManager } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { DataBenutzer } from "~/apps/schule/benutzerverwaltung/DataBenutzer";

	const props = defineProps<{
		data: DataBenutzer;
		benutzergruppen: BenutzergruppeListeEintrag[];
	}>();

	const manager: ComputedRef<BenutzerManager | undefined> = computed(() => {
		return props.data.manager;
	});

	const selected: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return props.benutzergruppen.length === manager.value?.anzahlGruppen() ?  true : false;
		},
		set(value: boolean) {
			if (value)
				void props.data.addBenutzergruppenBenutzer(props.benutzergruppen);
			else
				void props.data.removeBenutzergruppenBenutzer(props.benutzergruppen);
		}
	});

</script>
