<template>
	<svws-ui-modal :show="show" size="small" class="hidden">
		<template #modalTitle>Hinweis</template>
		<template #modalDescription>
			{{ text }}
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="abbrechen">OK</svws-ui-button>
			<svws-ui-button type="primary" @click="activate_recovery">Zu den Klausurvorgaben</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { Ref} from 'vue';

	const props = withDefaults(defineProps<{
		show: () => Ref<boolean>;
		text: string | undefined;
		jumpTo: () => Promise<void>;
	}>(), {
	});

	async function activate_recovery() {
		props.show().value = false;
		await props.jumpTo();
	}

	async function abbrechen() {
		props.show().value = false;
	}



</script>
