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
						<s-benutzergruppen-listeneintrag :bgle="bgle"
							:add-benutzer-to-benutzergruppe="addBenutzerToBenutzergruppe"
							:remove-benutzer-from-benutzergruppe="removeBenutzerFromBenutzergruppe"
							:manager="manager" />
					</template>
				</tbody>
			</table>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { BenutzergruppeListeEintrag, BenutzerManager } from "@svws-nrw/svws-core-ts";
	import { computed, WritableComputedRef } from "vue";

	const props = defineProps<{		benutzergruppen: BenutzergruppeListeEintrag[];
		manager : BenutzerManager;
		addBenutzerToBenutzergruppe : (bg_id : number) => Promise<void>;
		removeBenutzerFromBenutzergruppe	: (bg_id : number) => Promise<void>;
	}>();

	const selected: WritableComputedRef<boolean> = computed({
		get: () => props.benutzergruppen.length === props.manager.anzahlGruppen() ?  true : false,
		set: (value) => {
			if (value)
				void props.addBenutzerToBenutzergruppe(-1);
			else
				void props.removeBenutzerFromBenutzergruppe(-1);
		}
	});

</script>
