<template>
	<a class="app--menu--initials" href="#" @click.prevent="onClick">
		<svws-ui-tooltip position="right" v-if="user" :indicator="false">
			<div class="app--menu--initials--icon" :class="{'svws-is-admin-client': isAdminClient}">
				<template v-if="user.length > 5">{{ user.split(' ').map((username) => username[0]).join('').toUpperCase() }}</template>
				<template v-else>{{ user.slice(0, 2).toUpperCase() }}</template>
			</div>
			<template #content>
				<div class="app--menu--initials--label">
					Angemeldet als {{ user }}
					<template v-if="schule">
						<br>
						<span class="opacity-50">{{ schule }}</span>
					</template>
					<template v-if="schema">
						<br>
						<span class="opacity-50">DB: {{ schema }}</span>
					</template>
				</div>
			</template>
		</svws-ui-tooltip>
	</a>
</template>

<script setup lang='ts'>
	const props = withDefaults(defineProps<{
		collapsed?: boolean;
		user?: string;
		schule?: string;
		schema?: string;
		isAdminClient?: boolean;
	}>(), {
		collapsed: false,
		user: undefined,
		schule: undefined,
		schema: undefined,
		isAdminClient: false,
	});

	const emit = defineEmits<{
		(e: 'click', event: MouseEvent): void;
	}>();

	function onClick(event: MouseEvent) {
		emit("click", event);
	}
</script>
