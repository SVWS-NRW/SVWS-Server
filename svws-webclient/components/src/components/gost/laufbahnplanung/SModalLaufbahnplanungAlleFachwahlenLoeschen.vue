<template>
	<div>
		<svws-ui-button @click="toggle_modal" size="small" type="transparent" class="hover--danger">
			<span class="icon-sm i-ri-delete-bin-line" />
			<template v-if="hatFesteWahlen">Nicht feste Schüler-Fachwahlen zurücksetzen</template>
			<template v-else>Alle Schüler-Fachwahlen zurücksetzen</template>
		</svws-ui-button>
		<svws-ui-modal v-model:show="show" size="medium" type="danger">
			<template #modalTitle>
				<template v-if="hatFesteWahlen">Nicht feste Fachwahlen aller Schüler im Abiturjahrgang auf die Vorlage zurücksetzen</template>
				<template v-else>Die Fachwahlen aller Schüler im Abiturjahrgang auf die Vorlage zurücksetzen</template>
			</template>
			<template #modalDescription>
				<div class="flex gap-1 mb-2">
					<template v-if="hatFesteWahlen">
						Sollen für alle Schüler des Abiturjahrgangs die nicht festen Fachwahlen der noch in Planung befindlichen Halbjahre
						gelöscht und auf die Vorlage zurückgesetzt werden?
					</template>
					<template v-else>
						Sollen für alle Schüler des Abiturjahrgangs die Fachwahlen auf die Vorlage zurückgesetzt werden?
					</template>
				</div>
			</template>
			<template #modalActions>
				<svws-ui-button @click="toggle_modal" type="secondary">Abbrechen</svws-ui-button>
				<svws-ui-button @click="reset_fachwahlen" type="danger">Ja</svws-ui-button>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { GostJahrgangsdaten } from '../../../../../core/src/core/data/gost/GostJahrgangsdaten';

	const props = defineProps<{
		gostJahrgangsdaten: () => GostJahrgangsdaten;
		resetFachwahlen: () => Promise<void>;
	}>();

	const hatFesteWahlen = computed<boolean>(() => {
		const jg = props.gostJahrgangsdaten().jahrgang;
		return (jg === "Q1") || (jg === "Q2") || ((jg === "EF") && (props.gostJahrgangsdaten().istBlockungFestgelegt[0]));
	});

	const show = ref<boolean>(false);

	function toggle_modal() {
		show.value = !show.value;
	}

	async function reset_fachwahlen() {
		show.value = false;
		await props.resetFachwahlen();
	}

</script>
