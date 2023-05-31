<script setup lang='ts'>
	const props = withDefaults(defineProps<{
		collapsed?: boolean;
		user?: string;
		schule?: string;
	}>(), {
		collapsed: false,
		user: undefined,
		schule: undefined,
	});

	const emit = defineEmits<{
		(e: 'click', event: MouseEvent): void;
	}>();

	function onClick(event: MouseEvent) {
		emit("click", event);
	}
</script>

<template>
	<a class="app--menu--initials"
		href="#" @click.prevent="onClick">
		<svws-ui-tooltip position="right" v-if="user">
			<div class="app--menu--initials--icon">
				{{ user.charAt(0).toUpperCase() }}
			</div>
			<template #content>
				<div class="app--menu--initials--label">
					Angemeldet als {{ user }}
					<template v-if="schule">
						<br>
						<span class="opacity-50">{{ schule }}</span>
					</template>
				</div>
			</template>
		</svws-ui-tooltip>
	</a>
</template>

<style>
.app--menu--initials {
	@apply flex flex-col items-center w-full justify-center mx-auto relative;
}

.app--menu--initials .app--menu--initials--icon {
	@apply flex flex-col items-center w-full justify-center mx-auto relative;
	@apply rounded-full overflow-hidden bg-svws-950 text-white font-bold;
	@apply w-12 h-12 xl:w-14 xl:h-14;
}

.app--menu--initials svg {
	@apply w-3/4 mx-auto hidden;
}
</style>
