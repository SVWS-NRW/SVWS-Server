<template>
	<svws-ui-button type="danger" @click="removeZeitraster([...props.stundenplanManager().getListZeitrasterZuStunde(props.item)])"> {{ item }}. Stunde entfernen </svws-ui-button>
</template>

<script setup lang="ts">
	import type { StundenplanManager, StundenplanZeitraster} from "@core";

	const props = defineProps<{
		item: number;
		stundenplanManager: () => StundenplanManager;
		patchZeitraster: (data: Partial<StundenplanZeitraster>, zeitraster: StundenplanZeitraster) => Promise<void>;
		removeZeitraster: (multi: StundenplanZeitraster[]) => Promise<void>;
	}>();

</script>
